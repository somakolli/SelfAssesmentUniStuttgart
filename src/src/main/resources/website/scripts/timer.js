/**
 * sets up a timer according to given time limit
 * @param {number} limit 
 */
function startTimer(limit) {
    $("#timer").css("visibility", "visible");
    var x = setInterval(function () {
        limit--;

        $("#timer").text("Timer: " + limit + "s ");
        $(".bottom-right").click(function(){
            clearInterval(x);
        });
        if (limit <= 0) {
            clearInterval(x);
            $("#timer").text("EXPIRED");
            updateState();
        }
    }, 1000);
}
