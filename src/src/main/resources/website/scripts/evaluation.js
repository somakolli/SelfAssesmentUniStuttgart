const example = "001001100000101100011110";

/**
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
    $.get("questions/evaluation.json", function (data) {
        //let evaObj = $.parseJSON(data);
        //$("body").prepend(data.header);
        $("#evaluation").append(data.categories);
        $(data.header).insertBefore("#evaluation");
        let answersAndCount = stateToAnswers(state);
        let result = evaluate(getSolution(), answersAndCount[0], answersAndCount[1]);
        let fazitData = setUpEvaluation(getCategories(), result);
    });
    $(document).ready(function(){
        /* let answersAndCount = stateToAnswers(state);
        let result = evaluate(getSolution(), answersAndCount[0], answersAndCount[1]);
        let fazitData = setUpEvaluation(getCategories(), result); */
    })
    //addFazit(fazitData);
}

//convert state to binary string of answers
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
 * check for each individual question, if it was answered correctly
 * returns an array of 1's and 0's in order of the questions, 1 representing a correctly answered question etc.
 */
function evaluate(solution, answers, allAnswers) {
    console.log("ans: " + answers)
    console.log("sol: " + solution)
    let result = [];
    let pointer = 0;
    for(let i = 0; i < allAnswers.length; i++){
        let currCorrect = true;
        //pointer indicates the beginn of all answers for one indiv. question, pointer + allAnswers[i] the end
        for(let j = pointer; j < pointer + allAnswers[i]; j++){
            if(solution.charAt(j) != answers.charAt(j)){
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
 * get the idnividual fractions of rightly answered questions for each category 
 * collect fractions to choose the fitting fazit later on
 */
function setUpEvaluation(categories, result) {
    let j = 0;
    let overall = [];
    //value corresponds with the total amount of questions for this category
    for (let [key, value] of categories) {
        //console.log("value: " + value)
        let correct = 0;
        //j + value includes all questions that the current category consists of
        for (let i = j; i < j + value; i++) {
            //sum up the 1's (correctly answered question) for each category
            correct += result[i];
        }
        showFraction(key, correct, value);
        overall.push([correct, value]);
        j += value;
    }
    return overall;
}

//load fractions into evaluation page
function showFraction(id, correct, total) {
    let fraction = correct / total;
    let width = Math.max(fraction * 100, 5);
    let fillWidth = 100 - width;
    console.log(width);
    if (fraction < 0.25) {
        $(id).append('<div class="progressBarLow" style="width:' + width + '%">' + correct + '</div>');
    } else if (fraction >= 0.25 && fraction < 0.75) {
        $(id).append('<div class="progressBarAverage" style="width:' + width + '%">' + correct + '</div>');
    } else {
        $(id).append('<div class="progressBarGood" style="width:' + width + '%">' + correct + '</div>');
    }
    if(width < 100){
        $(id).append('<div class="progressFill" style="width:' + fillWidth +  '%">' + total + '</div>');
    }
}

function addFazit(fazitData){
    $("#fazit").append();
}

//stateToAnswers(example);



    