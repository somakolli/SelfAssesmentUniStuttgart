$(document).ready(function () {
    loadQuestionOrEvaluation();
});

function loadQuestionOrEvaluation(){
    let currentQuestionNumber = stateToQuestionByteArray().length;
    if(currentQuestionNumber === getQcount()){
        loadEvaluation(stateToBinaryString());
    } else {
        loadQuestion(currentQuestionNumber);
    }
}
//pushes the current question into the state und loads next question
function updateState() {
    let stateObj = { state: "" };
    let questionByteArray = stateToQuestionByteArray();
    questionList.push(Integer.parseInt(answerListToBitString(getAnswerList()),2));
    let questionListString = base64StringFromBytes(questionList);
    $("#timer").remove();
    history.pushState(stateObj, "new State", "?s=" + questionListString;
    loadQuestionOrEvaluation();
}

function getStateString(){
    let url = new URL(window.location.href);
    return url.searchParams.get("s");
}

function byteArrayToBitStringArray(byteArray){
    let bitStringArray = [];
    for(let i = 0; i < byteArray.length; i++){
        bitStringArray.push(('0000000' + byteArray[i].toString(2)).slice(-8));
    }
    return bitStringArray;
}
const base64Alphabet =
    'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

const base64url = {
        label: 'Standard \'base64url\' (RFC 4648 §5)',
        description: 'URL and Filename Safe Alphabet',
        alphabet: base64Alphabet + '-_',
        padCharacter: '=',
        padCharacterOptional: true,
        foreignCharactersForbidden: true
    };

/**
 * Returns bytes from given base64 string.
 * @param {string} string Base64 string
 * @return {Uint8Array} Bytes
 */
function bytesFromBase64String (string) {
    const options = base64url;
    const alphabet = options.alphabet;

    // translate each character into an octet
    const length = string.length;
    const octets = [];
    let character, octet;
    let i = -1;

    // go through each character
    while (++i < length) {
        character = string[i];

        if (options.lineSeparator &&
            character === options.lineSeparator[0] &&
            string.substr(i, options.lineSeparator.length) ===
            options.lineSeparator) {
            // this is a line separator, skip it
            i = i + options.lineSeparator.length - 1
        } else if (character === options.padCharacter) {
            // this is a pad character, ignore it
        } else {
            // this is an octet or a foreign character
            octet = alphabet.indexOf(character);
            if (octet !== -1) {
                octets.push(octet)
            } else if (options.foreignCharactersForbidden) {
                throw new ByteEncodingError(
                    `Forbidden character '${character}' at index ${i}`)
            }
        }
    }


    /**
     * Returns base64 string representing given bytes.
     * @param {Uint8Array} bytes Bytes
     * @return {string} Base64 string
     */
    function base64StringFromBytes(bytes) {
        const options = base64url;
        const alphabet = options.alphabet;
        const padCharacter = !options.padCharacterOptional && options.padCharacter
            ? options.padCharacter : '';

        // encode each 3-byte-pair
        let string = '';
        let byte1, byte2, byte3;
        let octet1, octet2, octet3, octet4;

        for (let i = 0; i < bytes.length; i += 3) {
            // collect pair bytes
            byte1 = bytes[i];
            byte2 = i + 1 < bytes.length ? bytes[i + 1] : NaN;
            byte3 = i + 2 < bytes.length ? bytes[i + 2] : NaN;

            // bits 1-6 from byte 1
            octet1 = byte1 >> 2;

            // bits 7-8 from byte 1 joined by bits 1-4 from byte 2
            octet2 = ((byte1 & 3) << 4) | (byte2 >> 4);

            // bits 4-8 from byte 2 joined by bits 1-2 from byte 3
            octet3 = ((byte2 & 15) << 2) | (byte3 >> 6);

            // bits 3-8 from byte 3
            octet4 = byte3 & 63;

            // map octets to characters
            string +=
                alphabet[octet1] +
                alphabet[octet2] +
                (!isNaN(byte2) ? alphabet[octet3] : padCharacter) +
                (!isNaN(byte3) ? alphabet[octet4] : padCharacter)
        }

        if (options.maxLineLength) {
            // limit text line length, insert line separators
            let limitedString = '';
            for (let i = 0; i < string.length; i += options.maxLineLength) {
                limitedString +=
                    (limitedString !== '' ? options.lineSeparator : '') +
                    string.substr(i, options.maxLineLength)
            }
            string = limitedString
        }

        return string
    }

    let byteArrayToState = function (byteArray){
        return base64StringFromBytes(byteArray)
    };
    let answerListToByte = function (answerList) {
        let bitString = "";
        for (const answer in answerList) {
            bitString += answerList[answer].checked ? "1" : "0";
        }
        return bitString;
    };

    let stateToQuestionByteArray = function () {
        return bytesFromBase64String(getStateString());
    };
}