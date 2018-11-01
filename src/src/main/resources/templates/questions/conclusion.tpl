[
    #foreach($conclusion in $conclusions)
    {
        "range" : $conclusion.getRange(),
        "conclusion": "$conclusion.getContent()"
    },
    #end

]