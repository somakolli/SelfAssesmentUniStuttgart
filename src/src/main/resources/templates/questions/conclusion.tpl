{
    "conclusion_array": [#foreach($conclusion in $conclusions)$conclusion.getRange()#if( $foreach.hasNext ),#end#end]
}