let categories = new Map;

#foreach($category in $categoryMap.entrySet())
categories.set("#$category.getKey().getCategoryName()", $category.getValue().size());
#end


function setCategories(cg){
    categories = cg;
}

function getCategories(){
    return categories;
}