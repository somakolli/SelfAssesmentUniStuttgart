//var currentPage = getState(); <-- implement so that getState() returns position of current question in question array
var currentPage = -1;

//hard coded categories of the test
var categories = ["Studium Allgemein", "Mathematik", "Theoretische Informatik", "Technische Informatik", "Memes"];

//dummy data
var content = [
    //insert overview page with active row 'Mathematik'. Use as template (copy everything inside and including the curly braces, append to the content array, seperating entries with a comma)
    {
        "Type": "overview",
        "categories": categories,
        "active": "Mathematik"
    },
    //actual content: a single question page. Use as template (copy everything inside and including the curly braces, append to the content array, seperating entries with a comma)
    {
        "questionID": "question_1",
        "Type": "singleQuestion",
        "questionString": ["Wieso möchten Sie Informatik studieren?"],
        "timer": true,
        "answers": [
            {
                "answerID": "answer1_1",
                "answerVal": "option1_1",
                "answer": "Wieso nicht?"
            },
            {
                "answerID": "answer1_2",
                "answerVal": "option1_2",
                "answer": "Es gibt nichts besseres"
            },
            {
                "answerID": "answer1_3",
                "answerVal": "option1_3",
                "answer": "Memes"
            }
        ],
        "category": "Mathematik"
    },
    //insert overview page with active row 'Meme'. Use as template (copy everything inside and including the curly braces, append to the content array, seperating entries with a comma)
    {
        "Type": "overview",
        "categories": categories,
        "active": "Memes"
    },
    //actual content: a multi question page. Each of the sub-questions is an actual question, but dislayed as a joint entity. Use as template (copy everything inside and including the curly braces, append to the content array, seperating entries with a comma)
    {
        "questionID": ["question_2", "question_3", "question_4"],
        "Type": "multiQuestion",
        "questionString": ["Wie gut schätzen sie Ihre mathematischen Fähgikeiten ein?", "Wie gut schätzen Sie Ihre technischen Fähigkeiten ein?", "Wie gut schätzen Sie Ihre praktischen Fähigkeiten ein?"],
        "timer": false,
        "answers": [
            {
                "answerID": "answer2_1",
                "answerVal": "option2_1",
                "answer": "Sehr gut"
            },
            {
                "answerID": "answer2_2",
                "answerVal": "option2_2",
                "answer": "Sehr sehr gut"
            },
            {
                "answerID": "answer3_3",
                "answerVal": "option3_3",
                "answer": "Dude trust me"
            }
        ],
        "category": "Memes"
    }
]

setUpPage(1);

function setUpPage(dir) {
    //weird cases: go back without predec. or go to next page without succec.
    if (currentPage <= 0 && dir == -1) {
        alert("Stop going back! Nothing to be seen here.")
        return null;
    } else if (currentPage >= content.length - 1 && dir == 1) {
        alert("That's it folks!")
        return null;
    }
    //initialize page with header and progressbar
    if (currentPage == -1) {
        initializeProgressBar(content.length);
        initializeHeader(categories);
    } else {
        var pageState = " ";
        if (content[currentPage].Type == "singleQuestion") {
            pageState = saveSinglePageState();
        } else if (content[currentPage].Type == "multiQuestion") {
            pageState = saveMultiPageState();
        }
    }
    //go to next page
    if (dir == 1) {
        currentPage++;
        incrementProgressBar();
        //go back a page
    } else {
        currentPage--;
        decrementProgressBar();
    }
    //current page displays a single question
    if (content[currentPage].Type == "singleQuestion") {
        showProgressBar();
        showHeader();
        insertSingleQuestion(content[currentPage].questionID, content[currentPage].questionString);
        insertAnswers(content[currentPage].questionID, content[currentPage].answers);
        setHeader(content[currentPage].category);
        addStyleSheet("singleQuestion");
        //current page displays a multi question
    } else if (content[currentPage].Type == "multiQuestion") {
        showProgressBar();
        showHeader();
        insertMultiQuestion(content[currentPage].questionID, content[currentPage].questionString, content[currentPage].answers);
        setHeader(content[currentPage].category);
        addStyleSheet("multiQuestion");
        //current page displays the overview
    } else if (content[currentPage].Type == "overview") {
        insertOverview(content[currentPage].categories, content[currentPage].active);
        hideProgressBar();
        //hideHeader();
        setHeader(content[currentPage].active);
        addStyleSheet("overview");
    }
    //set the header according to the category of the currently displayed question
    //set the timer if needed
    if (content[currentPage].timer == true) {
        setTimer(1);
    } else {
        setTimer(0);
    }
}


function initializeProgressBar(nr) {
    for (var i = 0; i < nr; i++) {
        $(".progress").append('<div class="progress-bar-inactive"></div>');
    }
}

function initializeHeader(categories) {
    for (var i = 0; i < categories.length; i++) {
        var firstThird = '<li id="';
        var secondThird = '" class="breadcrumb-item"> <a href="#">';
        var lastThird = '</a> </li>';
        $(".breadcrumb").append(firstThird + categories[i] + secondThird + categories[i] + lastThird);
    }
}

function incrementProgressBar() {
    if ($("#last-added").length) {
        $("#last-added").remove();
        $(".progress").prepend('<div  class="progress-bar-active"></div>');
        $(".progress").prepend('<div id="last-added" class="progress-bar-active"></div>');
    } else {
        $(".progress").prepend('<div id="last-added" class="progress-bar-active"></div>');
    }
    $(".progress").children().last().remove();
}

function decrementProgressBar() {
    $("#last-added").remove();
    $(".progress").children().first().attr("id", "last-added");
    $(".progress").append('<div class="progress-bar-inactive"></div>');
}

function setHeader(category) {
    if ($(".breadcrumb-item.active").length) {
        var former = $(".breadcrumb-item.active").attr("id");
        var predec = $(".breadcrumb-item.active").prev();
        $(".breadcrumb-item.active").remove();
        var firstThird = '<li id="';
        var secondThird = '" class="breadcrumb-item"> <a href="#">';
        var lastThird = '</a> </li>';
        $(firstThird + former + secondThird + former + lastThird).insertAfter(predec);
    }
    var predec = $("#" + category).prev();
    $("#" + category).remove();
    if ($(predec).length) {
        $('<li id="' + category + '" class="breadcrumb-item active" aria-current="PI" style="color:black"> <a>' + category + '</a> </li>').insertAfter(predec);
    } else {
        $(".breadcrumb").prepend('<li id="' + category + '" class="breadcrumb-item active" aria-current="PI" style="color:black"> <a>' + category + '</a> </li>');
    }

}

function showProgressBar(){
    $(".progress").show();
}

function showHeader(){
    $(".header").show();
}

function hideProgressBar(){
    $(".progress").hide();
}

function hideHeader(){
    $(".header").hide();
}

function addStyleSheet(identifier) {
    if(identifier === "singleQuestion"){
        if(!$('link[href="css/containerStyle.css"]').length){
            $('head').append('<link rel="stylesheet" type="text/css" href="css/containerStyle.css">');
        }
        if($('link[href="css/overviewStyle.css"]').length){
            $('link[href="css/overviewStyle.css"]').remove();
        }
    }
    else if (identifier === "multiQuestion") {
        if(!$('link[href="css/containerStyle.css"]').length){
            $('head').append('<link rel="stylesheet" type="text/css" href="css/containerStyle.css">');
        }
        if(!$('link[href="css/multiCheckboxStyle.css"]').length){
            $('head').append('<link rel="stylesheet" type="text/css" href="css/multiCheckboxStyle.css">');
        }
        if($('link[href="css/overviewStyle.css"]').length){
            $('link[href="css/overviewStyle.css"]').remove();
        }
    }
    else if (identifier === "overview") {
        if(!$('link[href="css/overviewStyle.css"]').length){
            $('head').append('<link rel="stylesheet" type="text/css" href="css/overviewStyle.css">');
        }
        if($('link[href="css/multiCheckboxStyle.css"]').length){
            $('link[href="css/multiCheckboxStyle.css"]').remove();
        }
        if($('link[href="css/containerStyle.css"]').length){
            $('link[href="css/containerStyle.css"]').remove();
        }
    } /* else if (identifier === "evaluation") {
        $('head').append('<link rel="stylesheet" type="text/css" href="css/evaluationStyle.css">');
        $('link[href="css/multiCheckboxStyle.css"]').remove();
    } */
}
