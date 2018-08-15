function insertImageAnswers(questionID, answers) {
    for (var i = 0; i < answers.length; i++) {
        var answerID = answers[i].answerID;
        var answerVal = answers[i].answerVal;
        var answer = answers[i].answer;
        var insert = '<li class="list-group-item"> <div class="radio"> <label> <input type="radio"  id="' + answerID + '" value="' + answerVal + '">' + answer + '</label> </div> </li>'
        $("#" + questionID).append(insert);
    }
}

function insertImageQuestion(questionID, questionString, img_src) {
    var beforeString = '<div class="card border-dark mb-3"> <div class="card-header"> <h5>';
    var afterString = '</h5> </div> <div id="imageContainer"> <img style="max-width: 100%; max-height: 100%"class="img-responsive" src="';
    var beforeID = '"> </div> <div class="card-body"> <ul id="';
    var afterID = '" class="list-group list-group-flush">';
    var botPiece = '</ul> </div> </div>'
    $(".card.border-dark.mb-3").replaceWith(beforeString + questionString + afterString + img_src + beforeID + questionID + afterID + botPiece);
}