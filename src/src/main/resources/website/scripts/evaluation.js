const example = "001001100000101100011110";

/**
 * 0) input: state as binary string
 * 1) replace question container with evaluation specific main div
 * 2) load category specific divs into main div from file
 * 3) calculate the result of userAnswers AND solution
 * 4) use calculation to setUp the evaluation page
 * 5) use calculation to choose fitting fazit
 */
function loadEvaluation(state) {
    $(".container-fluid").replaceWith('<div class="accordion" id="evaluation"></div>');
    $(".header").remove();
    $(".progress").remove();
    $('head').append('<link rel="stylesheet" type="text/css" href="css/evaluationStyle.css">');
    fillEvaluation();
    let answersAndCount = stateToAnswers(state);
    let result = evaluate(getSolution(), answersAndCount[0], answersAndCount[1]);
    let concData = setUpEvaluation(getCategories(), result, getQuestionPoints());
}

function fillEvaluation(categories) {
    $('<div class="jumbotron"> <h1 class="display-4" style="font-weight: bold">Evaluation</h1> <hr class="my-4"> <p>This is the evaluation of the SelfAssessment-Test.<br> The following table will show how well you did on each of the categories of the test by presenting the fraction of correctly given answers for each category of the test.<br> Click on the last field of the table "fazit" to view your feedback regarding the aforementioned results. </p> </div>').insertBefore("#evaluation")
    for (let [key, value] of categories.entries()) {
        $("#evaluation").append('<div class="card"> <div class="card-header"> <table class="table"> <tbody> <tr> <td class="category">' + key.slice(1) + '</td> <td class="bar"> <div id="' + key + '" class="progress"> </div> </td> </tr> </tbody> </table> </div> </div>')
    }
}

//input: state as binary string
//convert state to binary string of answers
//output: user answers as binary string, array of how many answer options each question consisted of
function stateToAnswers(state) {
    let alLength = 5;
    let nextQ = 0;
    let i = 0;
    let answerString = "";
    let allAnswers = [];
    while (i < state.length) {
        let answerCount = parseInt(state.substring(i, i + alLength), 2);
        allAnswers.push(answerCount);
        i += alLength;
        nextQ = i + answerCount;
        while (i < nextQ) {
            answerString += state[i];
            i++;
        }
        //console.log(answerString)
    }
    return [answerString, allAnswers];
}

/**
 * input: solution as binary string, user answers as binary string, array containing the info. how many answer options each single question consisted of
 * check for each individual question, if it was answered correctly
 * output: an array of 1's and 0's in order of the questions, 1 representing a correctly answered question etc.
 */
function evaluate(solution, answers, allAnswers) {
    console.log("ans: " + answers)
    console.log("sol: " + solution)
    let result = [];
    let pointer = 0;
    for (let i = 0; i < allAnswers.length; i++) {
        let currCorrect = true;
        //pointer indicates the beginn of all answers for one indiv. question, pointer + allAnswers[i] the end
        for (let j = pointer; j < pointer + allAnswers[i]; j++) {
            if (solution.charAt(j) != answers.charAt(j)) {
                currCorrect = false;
                break;
            }
        }
        currCorrect == true ? result.push(1) : result.push(0);
        pointer += allAnswers[i];
    }
    return result;
}

/**
 * input: map containing the id's of each category and how many questions it consisted of, the binary results of each individual question, the points of each question
 * get the idnividual fractions of rightly answered questions for each category 
 * collect fractions to choose the fitting fazit later on
 * output: overall score of the test
 */
function setUpEvaluation(categories, result, questionPoints) {
    let j = 0;
    let overall = 0;
    //let overall = [];
    //value corresponds with the total amount of questions for this category
    for (let [key, value] of categories) {
        //console.log("value: " + value)
        let correct = 0;
        let categoryTotal = 0;
        //j + value includes all questions that the current category consists of
        for (let i = j; i < j + value; i++) {
            //sum up the 1's (correctly answered question) for each category
            correct += result[i] * questionPoints[i];
            categoryTotal += questionPoints[i];
        }
        showFraction(key, correct, categoryTotal);
        overall += correct;
        //overall.push([correct, value]);
        j += value;
    }
    return overall;
}

//load fractions into evaluation page
function showFraction(id, correct, total) {
    let fraction = correct / total;
    let width = Math.max(fraction * 100, 5);
    let fillWidth = 100 - width;
    if (fraction < 0.25) {
        $(id).append('<div class="progressBarLow" style="width:' + width + '%">' + correct + '</div>');
    } else if (fraction >= 0.25 && fraction < 0.75) {
        $(id).append('<div class="progressBarAverage" style="width:' + width + '%">' + correct + '</div>');
    } else {
        $(id).append('<div class="progressBarGood" style="width:' + width + '%">' + correct + '</div>');
    }
    if (width < 100) {
        $(id).append('<div class="progressFill" style="width:' + fillWidth + '%">' + total + '</div>');
    }
}

function addConclusion(concData) {
    $.get("questions/conclusion.json", function (data) {
        for (let i = 0; i < data.length; i++) {
            if (concData <= data[i].range) {
                $("#fazit").append(data[i].conclusion);
                break;
            }
        }
    });
}

//stateToAnswers(example);



