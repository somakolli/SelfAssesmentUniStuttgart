
/* function saveQuestionState() {
    var url1 = window.location.href;
    url1 = url1.substring(url1.indexOf('#'), url1.length-1);
    var url = "";
    var questions = [];
    var inputs = $('body :input');
    inputs.each(function () {
        questions.push($(this).attr("id"));
        if($(this).is(":checked")){
            url += $(this).attr("id") + $(this).val() + "#";
        }
    });
    return url;
} */

/* function updateUrl(oldState) {
    var url = window.location.href + "#" + "hi";
    state = {
        "location" : url
    }
    console.log(oldUrl);
    history.replaceState(null, null, url);

    //window.location.href = url;
    //window.history.replaceState({}, "", url);
    return url;
} */

function updateUrl(oldState, questionState) {
    if (questionState) {
        var questionIDs = "";
        for(var i = 0; i < questionState.questionID.length; i++){
            questionIDs += questionState.questionID[i] + "/";
        }
        var answerIDs = "";
        for(var j = 0; j < questionState.checkedAnswers.length; j++){
            answerIDs += questionState.checkedAnswers[j] + "/";
        }
        var url = window.location.href + questionIDs + answerIDs;
        state = {
            "location": url
        }
        history.replaceState(null, null, url);
        return url;
    } else {
        return "";
    }
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
    //console.log(questionState);
    return questionState;
}



