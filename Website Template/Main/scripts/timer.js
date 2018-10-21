function setTimer(binary) { //delete this
    if (binary == 1) { //delete this
        $("#timer").show(); //delete this
        var limit = $("#timer").val();
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
        }, 1000);
    } else { //delete this
        $("#timer").hide(); //delete this
    } //delete this
} //delete this