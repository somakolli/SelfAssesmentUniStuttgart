<div class="progress">
    <!--Pseudocode-->
    #foreach(question in questionList) 
        <div  class="progress-bar-active"></div>
    #end
    <!--Pseudocode-->
    #for($range = [questionList.length+1...getTotalCount()])
        <div class="progress-bar-inactive"></div>
    #end
</div>