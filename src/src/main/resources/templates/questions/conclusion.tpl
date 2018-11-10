{
    "conclusion-array": [
        #foreach($conclusion in $conclusions)
        {
            "range" : $conclusion.getRange(),
            "conclusion": "$conclusion.getContent()"
        }#if( $foreach.hasNext ),#end
        #end
    ]
}