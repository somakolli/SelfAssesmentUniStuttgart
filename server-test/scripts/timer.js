/* var limit = $("#timer").val();
var currentTime = new Date();
currentTime.setMinutes(currentTime.getMinutes() + 1);
var x = setInterval(function () {

    var now = new Date().getTime();

    var distance = currentTime.getTime() - now;

    //var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
    var seconds = Math.floor((distance % (1000 * limit)) / 1000);

    $("#timer").text("Timer: " + seconds + "s ");

    if (distance <= 0) {
        clearInterval(x);
        $("#timer").text("EXPIRED");
    }
}, 1000); */

function startTimer(limit, x) {
    $("#timer").css("visibility", "visible");
    var currentTime = new Date();
    currentTime.setMinutes(currentTime.getMinutes() + 1);
    x = setInterval(function () {

        var now = new Date().getTime();

        var distance = currentTime.getTime() - now;

        //var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((distance % (1000 * limit)) / 1000);

        $("#timer").text("Timer: " + seconds + "s ");

        $(".bottom-right").click(function(){
            clearInterval(x);
        })
        if (distance <= 0) {
            clearInterval(x);
            $("#timer").text("EXPIRED");
            updateState();
        }
    }, 1000);
}

function hideTimer(x){
    clearInterval(x);
    $("#timer").css("visibility", "hidden");
}

