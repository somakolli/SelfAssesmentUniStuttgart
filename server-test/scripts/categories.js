let categories = new Map;

/* Example */
categories.set("#mathe", 1);
categories.set("#theo", 4);
/* Example */


function setCategories(cg){
    categories = cg;
}

function getCategories(){
    return categories;
}