/**
 * loads current question 
 * @param {number} currNum
 */
function loadQuestion(currNum) {
    $.get("questions/" + currNum + ".json", function (questionJson) {
        //let qObj = $.parseJSON(data);
        $.get("questions/" + currNum + ".html", function (questionHtml) {
            $(".container-fluid").children().first().replaceWith(questionHtml);
            setProgressBar(currNum);
            setHeader(currNum);
            if(questionJson.time > 0){
                $('<div id="timer"></div>').insertBefore(".bottom-right");
                startTimer(questionJson.time);
            }
        });
    });
}

//convert state into number of current question
/* function calcQNr(){
    return stateToAnswerList().length;
} */

/**
 * @param {String} state
 * @return {number} currNum
 */
function calcQNr(state){
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

/**
 * sets the progress bar according to current question number
 * @param {number} currNum 
 */
function setProgressBar(currNum){
    $(".progress").empty();
    for(let i = 0; i <= currNum; i++){
        $(".progress").append('<div class="progress-bar-active"></div>');
    }
    for(let j = 0; j < Qcount - currNum; j++){
        $(".progress").append('<div class="progress-bar-inactive"></div>');
    }
}

/**
 * sets header according to current question number
 * @param {number} currNum 
 */
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
            $(".breadcrumb").append('<li id="' + key + '" class="breadcrumb-item active" aria-current="PI" style="color:black"><a>' +  key  + '</a></li>')
        } else {
            $(".breadcrumb").append('<li id="' + key + '" class="breadcrumb-item"><a>' +  key  + '</a></li>');
        }
        i += value;
    }
}