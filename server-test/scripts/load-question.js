const exampleString = "00101101001111100010101010";

//console.log(calcQNr(exampleString));

//load currently to be shown question
function loadQuestion(currNum) {
    $.get("questions/" + currNum + ".json", function (data) {
        //let qObj = $.parseJSON(data);
        $(".container-fluid").children().first().replaceWith(data.question);
        if(data.time > 0){
            startTimer(data.time);
        } else {
            hideTimer();
        }
    })
}

//convert state into number of current question
/* function calcQNr(){
    return stateToAnswerList().length;
} */

function calcQNr(state){
    console.log("state: " + state)
    let alLength = 5;
    let currNum = 0;
    let i = 0;
    while(i < state.length){
        currNum++;
        let answerCount = alLength + parseInt(state.substring(i, i+alLength),2);
        i += answerCount;
    }
    return currNum;
}