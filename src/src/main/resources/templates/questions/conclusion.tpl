[
    #foreach($conslusion in $conclusions)
    {
        "range" : $conslusion.getRange(),
        "conclusion": "$conslusion.getContent()"
    },
    #end

]