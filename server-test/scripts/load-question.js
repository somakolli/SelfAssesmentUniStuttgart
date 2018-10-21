const exampleString = "00101101001111100010101010";

//console.log(calcQNr(exampleString));

//load currently to be shown question
function loadQuestion(currNum) {
    $.get("questions/" + currNum + ".json", function (data) {
        let qObj = $.parseJSON(data);
        $(".container-fluid").children().first().append(qObj.question);
        if(qObj.time > 0){
            startTimer(qObj.time);
        } else {
            hideTimer();
        }
    })
}

//convert state into number of current question
function calcQNr(state){
    let alLength = 4;
    let currNum = 0;
    let i = 0;
    while(i < state.length){
        currNum++;
        //console.log("number of answers: " + parseInt(state.substring(i, i+alLength),2))
        let answerCount = alLength + parseInt(state.substring(i, i+alLength),2);
        i += answerCount;
    }
    return currNum;
}

