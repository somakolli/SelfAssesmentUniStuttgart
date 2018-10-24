{
"time" : 30,
"question" : "<div class=\"card border-dark mb-3\"><div class=\"card-header\">$question.getQuestion()</div><div class=\"card-body\"><ul id=\"question$question.getId()\" class=\"list-group list-group-flush\">#foreach($answer in $question.getAnswers())<li class=\"list-group-item\"> <div class=\"checkbox\"> <label> <input type=\"checkbox\" id=\"answer$answer.getId()\" value=\"exampleAnswerValue\"> Example Answer </label> </div> </li> #end</ul></div> </div>",
"misc" : "<h1>$question.getQuestion()<\/h1>\r\n#foreach($answer in $question.getAnswers())<label for=\"0\">$answer.getContent()<\/label><input id=\"answer$answer.getId()\" type=\"checkbox\" value=\"0\"><br>\r\n#end<br>"
    }