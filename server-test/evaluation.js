var example = "001001100000101100011110";

function evaluate(solution, answers){
    var result = "";
    for(var i = 0; i < solution.length; i++){
        result += solution.charAt(i) == answers.charAt(i) ? "1" : "0";

    }
    alert("solution: " + solution + "\nyour answers " + answers + "\nyour score: " + result);
    return result;
}


function stateToAnswers(state){
    var alLength = 5;
    var nextQ = 0;
    var i = 0;
    var answerString = "";
    while(i < state.length){
        var answerCount = parseInt(state.substring(i, i+alLength),2);
        i += alLength;
        nextQ = i + answerCount;
        while(i < nextQ){
            answerString += state[i];
            i++;
        }
        //console.log(answerString)
    }

    return answerString;
}

stateToAnswers(example);
