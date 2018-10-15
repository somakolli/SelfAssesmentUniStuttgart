const example = "001001100000101100011110";

function evaluate(solution, answers){
    let result = "";
    for(leti = 0; i < solution.length; i++){
        result += solution.charAt(i) == answers.charAt(i) ? "1" : "0";

    }
    alert("solution: " + solution + "\nyour answers " + answers + "\nyour score: " + result);
    return result;
}


function stateToAnswers(state){
    let alLength = 5;
    let nextQ = 0;
    let i = 0;
    let answerString = "";
    while(i < state.length){
        let answerCount = parseInt(state.substring(i, i+alLength),2);
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
