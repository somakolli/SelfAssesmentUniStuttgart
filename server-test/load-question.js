const exampleString = "00101101001111100010101010";

function loadQuestion(state) {
    let alLength = 4;
    let currNum = 0;
    let i = 0;
    while(i < state.length){
        currNum++;
        console.log("number of answers: " + parseInt(state.substring(i, i+alLength),2))
        let answerCount = alLength + parseInt(state.substring(i, i+alLength),2);
        i += answerCount;
    }
    $.get("questions/" + currNum + ".json", function (data) {
        let qObj = $.parseJSON(data);
        if(qObj.time.length){
            $("#timer").css("visibility", "visible");
            $("#timer").val(qObj.time);
        }
        $(".container-fluid").children().first().append(qObj.question);
    })
    return currNum;
}

console.log(loadQuestion(exampleString));