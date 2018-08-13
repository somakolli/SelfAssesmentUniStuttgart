function insertVideoAnswers(questionID, answers) {
    for (var i = 0; i < answers.length; i++) {
        var answerID = answers[i].answerID;
        var answerVal = answers[i].answerVal;
        var answer = answers[i].answer;
        var insert = '<li class="list-group-item"> <div class="radio"> <label> <input type="radio"  id="' + answerID + '" value="' + answerVal + '">' + answer + '</label> </div> </li>'
        $("#" + questionID).append(insert);
    }
}

function insertVideoQuestion(questionID, questionString, video_encode) {
    var beforeString = '<div class="card border-dark mb-3"> <div class="card-header"> <h5>';
    var afterString = '</h5> </div> <div class="embed-responsive embed-responsive-16by9"> <video controls muted autoplay loop class="embed-responsive-item"> <source src="';
    var videoType = '" type="';
    var beforeID = '"> </video> </div> <div class="card-body"> <ul id="';
    var afterID = '" class="list-group list-group-flush">';
    var botPiece = '</ul> </div> </div>'
    $(".card.border-dark.mb-3").replaceWith(beforeString + questionString + afterString + video_encode.src + videoType + video_encode.video_type + beforeID + questionID + afterID + botPiece);
}