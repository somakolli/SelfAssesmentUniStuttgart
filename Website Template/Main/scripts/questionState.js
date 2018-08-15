
function updateUrl(questionState){
    var currState = window.history.state;
    console.log(currState);
    var newState = {
        "state" : []
    }
    if(currState != null){
        for(var i = 0; i < currState.state.length; i++){
            newState.state.push(currState.state[i]);
        }
    }
    if(questionState != null){
        newState.state.push([questionState.questionID, questionState.checkedAnswers]);
        history.replaceState(newState, null, "#");
    } else {
        history.replaceState(currState, null, "#");
    }
    console.log(window.history.state);
}

function saveSinglePageState() {
    var questionState = { "questionID": [$("ul").attr("id")], "checkedAnswers": [] }
    var inputs = $('body :input');
    inputs.each(function () {
        if ($(this).is(":checked")) {
            //push ids of answers
            questionState.checkedAnswers.push($(this).attr("id"));
        }
    });
    return questionState;
}

function saveMultiPageState() {
    var questionState = { "questionID": [], "checkedAnswers": [] }
    var questions = $("#questions").children();
    questions.each(function () {
        var current = $(this).children().first();
        questionState.questionID.push(current.attr("id"));
        var checked = [];
        while ($(current).next().length) {
            current = $(current).next();
            var input = $(current).find("input");
            if ($(input).is(":checked")) {
                checked.push($(input).attr("id"));
            }
        }
        questionState.checkedAnswers.push(checked);
    });
    return questionState;
}



