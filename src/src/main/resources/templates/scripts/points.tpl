points = [#foreach($question in $questions)$question.getPoints()#if( $foreach.hasNext ),#end#end]

function getQuestionPoints(){
    return points;
}