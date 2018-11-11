<!doctype html>
<html>

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Template for a content page</title>
    <style>
        $bootstrapCss
        $bottomContentStyleCss
        $colorCss
        $containerStyleCss
        $topContentStyleCss
    </style>
</head>

<body>
<div class="container-fluid">
<div class="card border-dark mb-3">
    <div class="card-header" style="text-align: left">$question.getContent()</div>
    <div class="card-body">
        <ul id="question$question.getId()" class="list-group list-group-flush">
            #foreach($answer in $question.getAnswers())
                <li class="list-group-item">
                    <div class="radio">
                        <label>
                            <input name="answer$answer.getId()" type="radio" id="answer$answer.getId()" value="answer$answer.getId()">
                            <div class="markdown-content">$answer.getContent()</div>
                        </label>
                    </div>
                </li>
            #end
        </ul>
    </div>
</div>
</div>
</body>
</html>