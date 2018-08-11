function insertOverview(categories, active){
    var fullPiece = '<div class="card border-dark mb-3"> <div class="card-header"> <h1>Überblick</h1> <p>Die folgende Tabelle dient als Überblick über die Inhalte des Tests und wird nach jedem Abschnitt eingeblendet.</p> </div> <div class="card-body"> <ul class="list-group"> </ul> </div> </div>';
    $(".card.border-dark.mb-3").replaceWith(fullPiece);
    for(var i = 0; i < categories.length; i++){
        var firstPiece = '<li id="' 
        var secondPiece = '" class="list-group-item list-group-item-secondary">';
        var thirdPiece = '</li>';
        //DOES NOT HANDLE DUPLICATES!!!
        if(categories[i] == active){
            $(".list-group").append(firstPiece + 'active' + secondPiece + categories[i] + thirdPiece);
        } else {
            $(".list-group").append(firstPiece + 'inactive' + secondPiece + categories[i] + thirdPiece);            
        }
    }
}