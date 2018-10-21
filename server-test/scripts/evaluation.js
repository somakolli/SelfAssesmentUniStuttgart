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
    $.get("questions/evluation.json", function (data) {
        let evaObj = $.parseJSON(data);
        $("body").append(evaObj.header);
        $("#evaluation").append(evaObj.categories);
    })
    let result = evaluate(getSolution(), stateToAnswers(state));
    let fazitData = setUpEvaluation(getCategories(), result);
    addFazit(fazitData);
}

//convert state to binary string of answers
function stateToAnswers(state) {
    let alLength = 5;
    let nextQ = 0;
    let i = 0;
    let answerString = "";
    while (i < state.length) {
        let answerCount = parseInt(state.substring(i, i + alLength), 2);
        i += alLength;
        nextQ = i + answerCount;
        while (i < nextQ) {
            answerString += state[i];
            i++;
        }
        //console.log(answerString)
    }
    return answerString;
}

//calculate how many answers were right by AND-ing binary answer string and binary solution string
function evaluate(solution, answers) {
    let result = "";
    for (leti = 0; i < solution.length; i++) {
        result += solution.charAt(i) == answers.charAt(i) ? "1" : "0";

    }
    //alert("solution: " + solution + "\nyour answers " + answers + "\nyour score: " + result);
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
        let correct = 0;
        for (let i = j; i < j + value; i++) {
            correct += result[j] == "1" ? 1 : 0;
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
    if (fraction < 0.25) {
        $(id).next().children().first().append('<div class="progressBarLow" style="width:' + fraction + '">' + correct + '</div>');
        $(id).next().children().first().append(total);
    } else if (fraction >= 0.25 && fraction < 0.75) {
        $(id).next().children().first().append('<div class="progressBarAverage" style="width:' + fraction + '">' + correct + '</div>');
        $(id).next().children().first().append(total);
    } else {
        $(id).next().children().first().append('<div class="progressBarGood" style="width:' + fraction + '">' + correct + '</div>');
        $(id).next().children().first().append(total);
    }
}

function addFazit(fazitData){
    $("#fazit").append();
}

stateToAnswers(example);
