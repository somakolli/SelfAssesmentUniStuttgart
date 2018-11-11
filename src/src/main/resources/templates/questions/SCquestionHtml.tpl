<div class="card border-dark mb-3">
    <div class="card-header" style="text-align: left">$question.getContent()</div>
    <div class="card-body">
        <ul id="question$question.getId()" class="list-group list-group-flush">
        #foreach($answer in $question.getAnswers())
            <li class="list-group-item">
                <div class="radio">
                    <label>
                        <input type="radio" id="answer$answer.getId()" value="">
                        <div class="markdown-content">$answer.getContent()</div>
                    </label>
                </div>
            </li>
        #end
        </ul>
    </div>
</div>
