
/**
 * given the binary encoded state,
 * calculate and display the evaluation page
 * @param {String} state
 */
function loadEvaluation(state) {
    $("#timer").remove();
    $(".container-fluid").replaceWith('<div class="accordion" id="evaluation"></div>');
    $(".header").remove();
    $(".progress").remove();
    $('head').append('<link rel="stylesheet" type="text/css" href="css/evaluationStyle.css">');
    fillEvaluation(getCategories());
    let answersAndCount = stateToAnswers(state);
    let result = evaluate(getSolution(), answersAndCount[0], answersAndCount[1]);
    let overallScore = setUpEvaluation(getCategories(), result, getQuestionPoints());
    addConclusion(overallScore);
}

/**
 * fill the evaluation page with the category fields and progress bars
 * @param {map} categories 
 */
function fillEvaluation(categories) {
    for (let [key, value] of categories.entries()) {
        $("#evaluation").append('<div class="card"> <div class="card-header"> <table class="table"> <tbody> <tr> <td class="category">' + key + '</td> <td class="bar"> <div id="' + key + '" class="progress"> </div> </td> </tr> </tbody> </table> </div> </div>')
    }
    $("#evaluation").append('<div class="card"> <div class="card-header"> <table class="table"> <tbody> <tr> <td style="text-align: center;"> <button class="btn btn-link collapsed" type="button" data-toggle="collapse" data-target="#fazitCollapse" aria-expanded="false" aria-controls="fazitCollapse" style="font-size: 24px;"> Fazit </button> </td> </tr> </tbody> </table> </div> <!-- aria-labelledby="headingThree" --> <div id="fazitCollapse" class="collapse"  data-parent="#accordionExample"> <div id="fazit" class="card-body"> </div> </div> </div>');
}

/**
 * produces an array consisting of the user answers as binary string 
 * and an array of how many answer options each question consisted of
 * @return {array} [useranswers, answersperquestion]
 */
function stateToAnswers() {
    let answerArray = stateToAnswerList();
    let answerString = "";
    let allAnswers = [];
    for(let i = 0; i < answerArray.length; i++){
        let currentAnswerString = answerArray[i];
        answerString+=currentAnswerString;
        allAnswers.push(currentAnswerString.length);
    }
    return [answerString, allAnswers];
}


/**
 * produces an array of 1's and 0's in order of the questions, 
 * 1 representing a correctly answered question 
 * @param {String} solution
 * @param {String} answers
 * @param {array}  evaluation
 * @return {array} result 
 */
function evaluate(solution, answers, allAnswers) {
    /* console.log("ans: " + answers)
    console.log("sol: " + solution) */
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
  * calculates the overall score of the test
  * @param {map} categories 
  * @param {array} result 
  * @param {array} questionPoints 
  * @return {number} overallPoints
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

/**
 * load html-fractions into evaluation page
 * @param {String} id 
 * @param {number} correct 
 * @param {number} total 
 */
function showFraction(id, correct, total) {
    let fraction = correct / total;
    let width = Math.max(fraction * 100, 5);
    let fillWidth = 100 - width;
    if (fraction < 0.25) {
        $('[id="' + id + '"]').append('<div class="progressBarLow" style="width:' + width + '%">' + correct + '</div>');
    } else if (fraction >= 0.25 && fraction < 0.75) {
        $('[id="' + id + '"]').append('<div class="progressBarAverage" style="width:' + width + '%">' + correct + '</div>');
    } else {
        $('[id="' + id + '"]').append('<div class="progressBarGood" style="width:' + width + '%">' + correct + '</div>');
    }
    if (width < 100) {
        $('[id="' + id + '"]').append('<div class="progressFill" style="width:' + fillWidth + '%">' + total + '</div>');
    }
}

/**
 * add a conclusion to the evaluation page
 * according to overall score
 * @param {number} overallScore
 */
function addConclusion(overallScore) {
    $.get("questions/conclusion.json", function (data) {
        let rangeArray = data.conclusion_array;
        for (let i = 0; i < rangeArray.length; i++) {
            if (overallScore <= rangeArray[i]) {
                $.get("conclusions/" + rangeArray[i] + ".html", function (conclusionHtml) {
                    $("#fazit").append(conclusionHtml);
                });
                break;
            }
        }
    });
}



