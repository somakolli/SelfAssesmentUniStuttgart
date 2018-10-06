function evaluate(solution, answers){
    var result = "";
    for(var i = 0; i < solution.length; i++){
        result += solution.charAt(i) == answers.charAt(i) ? "1" : "0";

    }
    return result;
}
