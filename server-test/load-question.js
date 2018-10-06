var exampleString = "00101101001111100010101010";

function loadQuestion(state) {
    var alLength = 4;
    var currNum = 0;
    var i = 0;
    while(i < state.length){
        currNum++;
        console.log("number of answers: " + parseInt(state.substring(i, i+alLength),2))
        var answerCount = alLength + parseInt(state.substring(i, i+alLength),2);
        i += answerCount;
    }
    /* $.get("questions/" + currNum + ".json", function (data) {

    }) */
    return currNum;
}

console.log(loadQuestion(exampleString));