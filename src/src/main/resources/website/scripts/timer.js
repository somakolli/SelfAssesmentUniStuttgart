/**
 * sets up a timer according to given time limit
 * @param {number} limit 
 */
function startTimer(limit) {
    $("#timer").css("visibility", "visible");
    var currentTime = new Date();
    currentTime.setMinutes(currentTime.getMinutes() + 1);
    var x = setInterval(function () {

        var now = new Date().getTime();

        var distance = currentTime.getTime() - now;

        //var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((distance % (1000 * limit)) / 1000);

        $("#timer").text("Timer: " + seconds + "s ");
        $(".bottom-right").click(function(){
            clearInterval(x);
        });
        if (distance <= 0) {
            clearInterval(x);
            $("#timer").text("EXPIRED");
            updateState();
        }
    }, 1000);
}
