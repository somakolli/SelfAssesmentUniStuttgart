/*
The MIT License (MIT)

Copyright 2018 [Fränz Friederes](https://fraenz.frieder.es/)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of
the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


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

        if (character === options.padCharacter) {
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

    // calculate original padding and verify it
    const padding = (4 - octets.length % 4) % 4;
    if (padding === 3) {
        throw new ByteEncodingError(
            `A single remaining encoded character in the last quadruple or a ` +
            `padding of 3 characters is not allowed`)
    }

    // fill up octets
    for (i = 0; i < padding; i++) {
        octets.push(0)
    }

    // map pairs of octets (4) to pairs of bytes (3)
    const size = octets.length / 4 * 3;
    const bytes = new Uint8Array(size);
    let j;
    for (i = 0; i < octets.length; i += 4) {
        // calculate byte index
        j = i / 4 * 3;
        // byte 1: bits 1-6 from octet 1 joined by bits 1-2 from octet 2
        bytes[j] = (octets[i] << 2) | (octets[i + 1] >> 4);
        // byte 2: bits 3-6 from octet 2 joined by bits 1-4 from octet 3
        bytes[j + 1] = ((octets[i + 1] & 15) << 4) | (octets[i + 2] >> 2);
        // byte 3: bits 1-2 from octet 3 joined by bits 1-6 from octet 4
        bytes[j + 2] = ((octets[i + 2] & 3) << 6) | octets[i + 3]
    }

    return bytes.slice(0, size - padding)
}

/**
 * Returns base64 string representing given bytes.
 * @param {Uint8Array} bytes Bytes
 * @return {string} Base64 string
 */
function base64StringFromBytes (bytes) {
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

function binaryStringFromBytes (bytes) {
    
    return Array.from(bytes)
        .map(byte => ('0000000' + byte.toString(2)).slice(-8))
        .join('')
}

/**
 * Returns bytes from given binary string.
 * @param {string} string Binary string
 * @return {Uint8Array} Bytes
 */
function bytesFromBinaryString (string) {

    // fill up leading zero digits
    if (string.length % 8 > 0) {
        string = ('0000000' + string).substr(string.length % 8 - 1)
    }

    // decode each byte
    const bytes = chunk(string, 8).map((byteString, index) => {
        const byte = parseInt(byteString, 2);
        if (byteString.match(/[0-1]{8}/) === null || isNaN(byte)) {
            throw new ByteEncodingError(
                `Invalid binary encoded byte '${byteString}'`)
        }
        return byte
    });

    return new Uint8Array(bytes)
}

function chunk (string, length) {
    return string !== '' ? string.match(new RegExp(`.{1,${length}}`, 'g')) : []
}

