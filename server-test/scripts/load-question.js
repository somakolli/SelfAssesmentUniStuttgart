const exampleString = "00101101001111100010101010";
var x;

//console.log(calcQNr(exampleString));

//load currently to be shown question
function loadQuestion(currNum) {
    $.get("questions/" + currNum + ".json", function (data) {
        //let qObj = $.parseJSON(data);
        $(".container-fluid").children().first().replaceWith(data.question);
        setProgressBar(currNum);
        setHeader(currNum);
        clearInterval(x);
        if(data.time > 0){
            $('<div id="timer"></div>').insertBefore(".bottom-right");
            startTimer(data.time, x);
        } /* else {
            hideTimer(x);
        } */
    });
}

//convert state into number of current question
/* function calcQNr(){
    return stateToAnswerList().length;
} */

function calcQNr(state){
    //console.log("state: " + state)
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

function setProgressBar(currNum){
    $(".progress").empty();
    for(let i = 0; i <= currNum; i++){
        $(".progress").append('<div class="progress-bar-active"></div>');
    }
    for(let j = 0; j < Qcount - currNum; j++){
        $(".progress").append('<div class="progress-bar-inactive"></div>');
    }
}

function setHeader(currNum){
    $(".breadcrumb").empty();
    let i = 0;
    let currCat = null;
    for(let [key, value] of getCategories()){
        for(let j = i; j < i + value; j++){
            if(j == currNum){
                currCat = key;
                break;
            }
        }
        if(currCat == key){
            $(".breadcrumb").append('<li id="' + key + '" class="breadcrumb-item active" aria-current="PI" style="color:black"><a>' +  key.substring(1)  + '</a></li>')
        } else {
            $(".breadcrumb").append('<li id="' + key + '" class="breadcrumb-item"><a>' +  key.substring(1)  + '</a></li>');
        }
        i += value;
    }
}