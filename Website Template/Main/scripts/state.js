function replaceSingleQuestion(){
    $(".card.border-dark.mb-3").replaceWith('<div class="card border-dark mb-3"> <table class="table table-bordered"> <thead> <tr> <th></th> <th>Sehr gut</th> <th>Sehr sehr gut</th> <th>Dude trust me</th> </tr> </thead> <tbody> <tr> <th>Wie gut schätzen Sie ihre mathematischen Fähigkeiten ein?</td> <td> <div class="radio"> <input type="radio" id="question1_first" name="option1" onclick="saveQuestionState(this)"> </div> </td> <td> <div class="radio"> <input type="radio" id="question1_second" name="option2" onclick="saveQuestionState(this)"> </div> </td> <td> <div class="radio"> <input type="radio" id="question1_third" name="option3" onclick="saveQuestionState(this)"> </div> </td> </tr> <tr> <th>Wie gut schätzen Sie ihre technischen Fähigkeiten ein?</td> <td> <div class="radio"> <input type="radio" id="question2_first" name="option1" onclick="saveQuestionState(this)"> </div> </td> <td> <div class="radio"> <input type="radio" id="question2_second" name="option2" onclick="saveQuestionState(this)"> </div> </td> <td> <div class="radio"> <input type="radio" id="question2_third" name="option3" onclick="saveQuestionState(this)"> </div> </td> </tr> <tr> <th>Wie gut schätzen Sie ihre pratkischen Fähigkeiten ein?</td> <td> <div class="radio" > <input type="radio" id="question3_first" name="option1" onclick="saveQuestionState(this)"> </div> </td> <td> <div class="radio"> <input type="radio" id="question3_second" name="option2" onclick="saveQuestionState(this)"> </div> </td> <td> <div class="radio"> <input type="radio" id="question3_third" name="option3" onclick="saveQuestionState(this)"> </div> </td> </tr> </tbody> </table> </div>');
    addStyleSheet("multi");
    incrementProgressBar();
    replaceNextButton();
    progressHeader();
}

function replaceMultiQuestion(){ 
    $(".progress").remove();
    $(".header").remove();
    $(".container-fluid").replaceWith('<div class="accordion" id="evaluation"> <div class="card"> <div class="card-header" id="headingOne"> <table class="table"> <tbody> <tr> <td class="category"> <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#collapseOne" aria-expanded="false" aria-controls="collapseOne"> Mathematik </button> </td> <td class="bar"> <div class="progress"> <div class="progressBarLow"> 15% </div> </div> </td> </tr> </tbody> </table> </div> <div id="collapseOne" class="collapse" aria-labelledby="headingOne" data-parent="#accordionExample"> <div class="card-body"> You suck. </div> </div> </div> <div class="card"> <div class="card-header" id="headingTwo"> <table class="table"> <tbody> <tr> <td class="category"> <button class="btn btn-link collapsed" type="button" data-toggle="collapse" data-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo"> Theoretische Informatik </button> </td> <td class="bar"> <div class="progress"> <div class="progressBarAverage"> 38% </div> </div> </td> </tr> </tbody> </table> </div> <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionExample"> <div class="card-body"> You still suck. Go away. </div> </div> </div> <div class="card"> <div class="card-header" id="headingThree"> <table class="table"> <tbody> <tr> <td class="category"> <button class="btn btn-link collapsed" type="button" data-toggle="collapse" data-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree"> Technische Informatik </button> </td> <td class="bar"> <!-- <h6> Sehr gut </h6> --> <div class="progress"> <div class="progressBarGood"> 60% </div> </div> </td> </tr> </tbody> </table> </div> <div id="collapseThree" class="collapse" aria-labelledby="headingThree" data-parent="#accordionExample"> <div class="card-body"> To mark territory, a male hippo will shit in the water it`s swimming in and proceed to swirl it around to spread the scent into the water. </div> </div> </div> </div> </div>'); 
    $('<div class="jumbotron"> <h1 class="display-4" style="font-weight: bold">Evaluation</h1> <hr class="my-4"> <p >This is the evaluation of the SelfAssessment-Test.<br> The following will explain how well you did on each of the categories of the test and conclude your aptitude regarding the subject you took the test for.<br> Click on the name of the category to view the corresponding feedback. </p> </div>').insertBefore("#evaluation");
    addStyleSheet("evaluation");
}

function incrementProgressBar(){
    $('<div class="progress-bar-active"></div>').insertBefore("#last-added");

}

//funktioniert scheinbar nicht für IE8
function addStyleSheet(identifier){
    if(identifier === "multi"){
        $('head').append('<link rel="stylesheet" type="text/css" href="css/multiCheckboxStyle.css">');
    } 
    else if(identifier === "evaluation"){
        $('head').append('<link rel="stylesheet" type="text/css" href="css/evaluationStyle.css">');
        $('link[href="css/multiCheckboxStyle.css"]').remove();
    }
}

function progressHeader(){
    var next = $("breadcrumb-item.active").next();
    $("breadcrumb-item.active").addClass("breadcrumb-item").removeClass("breadcrumb-item active");
    $(next).addClass("breadcrumb-item active").removeClass("breadcrumb-item");
}

function replaceNextButton(){
    $(".bottom-right").replaceWith('<div class="bottom-right"> <a href="#" class="next" onclick="replaceMultiQuestion();">Next &raquo;</a> </div>');
}