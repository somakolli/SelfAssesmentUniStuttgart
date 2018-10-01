<nav class="header" aria-label="breadcrumb">
        <ol class="breadcrumb">
        <!--Pseudocode-->
        #foreach(category in categoryList)
            <!--Pseudocode-->
            #if(category.type == active)
                <li id="" class="breadcrumb-item active" aria-current="PI" style="color:black">
                <!--Pseudocode-->
                <a> category.name </a> 
                </li>
            #else
                <li id="" class="breadcrumb-item">
                <!--Pseudocode--> 
                <a href="#"> category.name </a> 
                </li>
            #end
        </ol>
</nav>