function insertAnswerOptions(answer) {
        var option = '<th>' + answer + '</th>'
        $("#options").append(option);
}

function insertMultiQuestion(questionIDs, questions, answers) {
    var fullPiece = '<div class="card border-dark mb-3"> <table class="table table-bordered"> <thead> <tr id="options"> </tr> </thead> <tbody id="questions"> </tbody> </table> </div>'; 
    $(".card.border-dark.mb-3").replaceWith(fullPiece);
    insertAnswerOptions("");
    for(var i = 0; i < questions.length; i++){
        var insert = '<tr>';
        var questionID = questionIDs[i];
        var question = questions[i];
        insert += '<th id="' + questionID + '">' + question + '</th>';
        for(var j = 0; j < answers.length; j++){
            var answerID = answers[j].answerID;
            var answerVal = answers[j].answerVal;
            insert += '<td> <div class="radio"> <input type="radio" id="' + answerID+ '" value="' + answerVal + '"> </div> </td>';
        }
        insertAnswerOptions(answers[i].answer);
        insert += '</tr>';
        $("#questions").append(insert);
    }
}


