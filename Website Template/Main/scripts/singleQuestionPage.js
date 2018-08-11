function insertAnswers(questionID, answers) {
    for (var i = 0; i < answers.length; i++) {
        var answerID = answers[i].answerID;
        var answerVal = answers[i].answerVal;
        var answer = answers[i].answer;
        var insert = '<li class="list-group-item"> <div class="radio"> <label> <input type="radio"  id="' + answerID + '" value="' + answerVal + '">' + answer + '</label> </div> </li>'
        $("#" + questionID).append(insert);
    }
}

function insertSingleQuestion(questionID, questionString) {
    var topPiece = '<div class="card border-dark mb-3"> <div class="card-header"> <h5>';
    var beforeID = '</h5> </div> <div class="card-body"> <ul id="';
    var afterID = '" class="list-group list-group-flush">';
    var botPiece = '</ul> </div> </div>'
    $(".card.border-dark.mb-3").replaceWith(topPiece + questionString + beforeID + questionID + afterID + botPiece);
}


