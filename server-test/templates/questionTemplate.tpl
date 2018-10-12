<div class="card-header">
<h5>$question.getQuestion()</h5>
</div>
<div class="card-body">
<ul id="$question.getId()" class="list-group list-group-flush">
    #foreach($answer in $question.getAnswers())
    <li class="list-group-item">
        <div class="radio"><label> <input type="radio" id="$answer.getId()" value="exampleAnswerValue">$answer.getContent()</label> </div>
    </li>
    #end
</ul>
</div>