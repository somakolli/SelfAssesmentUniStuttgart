$(document).ready(function () {
    updateState();
    /* loadQuestion(0); */
});

function updateState() {
    $(document).ready(function () {
        let stateObj = { state: "" };
        let questionList = stateToAnswerList();
        questionList.push(answerListToBitString(getAnswerList()));
        let questionListString = answerListToBinary(questionList);
        if(questionListString == "00000"){
            loadQuestion(0);
            //history.pushState(stateObj, "new State", "?s=" + binaryStringToState(questionListString));
        } else {
            let curr = calcQNr(questionListString);
            //console.log("curr :" + curr + ", " + questionListString)
            if (getQcount() == curr) {
                loadEvaluation(questionListString);
            } else {
                loadQuestion(curr);
            }
            history.pushState(stateObj, "new State", "?s=" + binaryStringToState(questionListString));
        }

    });
}

let stateToBinaryString = function () {
    let url = new URL(window.location.href);
    let stateString = url.searchParams.get("s");
    if (stateString === null) {
        return {};
    } else {
        return atob(stateString);
    }
};
let binaryStringToState = function (binaryString) {
    //console.log("binaryString:", binaryString);
    return btoa(binaryString);
};
let getAnswerList = function () {
    let answersList = [];
    let answers = $("input[type=checkbox]");
    if (answers.length < 1) {
        answers = $("input[type=radio");
    }
    answers.each(function () {
        answersList.push({ id: this.id, checked: this.checked });
    });
    //console.log("answerListLength:", answersList.length);
    return answersList;
};
let answerListToBitString = function (answerList) {
    let bitString = "";
    for (const answer in answerList) {
        bitString += answerList[answer].checked ? "1" : "0";
    }

    return bitString;
};

let stateToAnswerList = function () {
    let stateBinaryString = stateToBinaryString();
    let i = 0;
    let answerList = [];
    let questionCount = 0;
    while (i < stateBinaryString.length) {
        let bitStringLength = parseInt(stateBinaryString.substring(i, i + 5), 2);
        //console.log("bitString: " + bitStringLength)
        i += 5;
        let answersSubstring = stateBinaryString.substring(i, i + bitStringLength);
        i += bitStringLength;
        answerList.push(answersSubstring);
        questionCount++;
    }
    return answerList;
};


//expects a answerList [answerBitString]
let answerListToBinary = function (answerList) {
    let stateString = "";
    for (let i = 0; i < answerList.length; i++) {
        let bitString = answerList[i];

        /*
        * reverse bitstring so parseInt() returns the correct bit string later
        */
        //let bitStringLength = bitString.length.toString(2).split("").reverse().join("");
        let bitStringLength = bitString.length.toString(2);
        //making length string larger if it is smaller than 5 bit
        let bitStringPrefix = "";
        for (let i = bitStringLength.length; i < 5; i++) {
            bitStringPrefix += "0";
        }
        /*
        * also convert the prefix to a suffix so the padding is still correct
        */
        //stateString += bitStringLength + bitStringPrefix + bitString;
        stateString += bitStringPrefix + bitStringLength + bitString;
    }
    return stateString;
};