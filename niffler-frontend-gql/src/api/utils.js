import * as crypto from 'crypto-js';
import sha256 from 'crypto-js/sha256';
import Base64 from 'crypto-js/enc-base64';

const base64Url = (str) => {
    return str.toString(Base64).replace(/=/g, '').replace(/\+/g, '-').replace(/\//g, '_');
}

const generateCodeVerifier = () => {
    return base64Url(crypto.enc.Base64.stringify(crypto.lib.WordArray.random(32)));
}

const generateCodeChallenge = () => {
    const codeVerifier = sessionStorage.getItem('codeVerifier');
    return base64Url(sha256(codeVerifier));
}

const getCookie = (name) => {
    const pattern = RegExp(name + "=.[^;]*")
    const matched = document.cookie.match(pattern)
    if (matched) {
        const cookie = matched[0].split('=')
        return cookie[1]
    }
    return null;
}


export {
    base64Url,
    generateCodeVerifier,
    generateCodeChallenge,
    getCookie
}
