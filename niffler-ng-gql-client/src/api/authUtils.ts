import * as crypto from "crypto-js";
import sha256 from "crypto-js/sha256";
import Base64 from "crypto-js/enc-base64";
import {JsonTokens} from "../types/JsonTokens.ts";
import {authorizeUrl} from "./url/auth.ts";

const base64Url = (str: string | crypto.lib.WordArray) => {
    return str.toString(Base64).replace(/=/g, "").replace(/\+/g, "-").replace(/\//g, "_");
}

const codeVerifier = () => {
    return base64Url(crypto.enc.Base64.stringify(crypto.lib.WordArray.random(32)));
}

const codeChallenge = (codeVerifier: string) => {
    return base64Url(sha256(codeVerifier!));
}

const codeVerifierFromLocalStorage = (): string => {
    return <string>localStorage.getItem('codeVerifier');
}

const codeChallengeFromLocalStorage = (): string => {
    return <string>localStorage.getItem('codeChallenge');
}

const idTokenFromLocalStorage = (): string => {
    return <string>localStorage.getItem('id_token');
}

const accessTokenFromLocalStorage = (): string => {
    return <string>localStorage.getItem('access_token');
}

const tokenFromUrlEncodedParams = (code: string, verifier: string) => {
    return new URLSearchParams({
        "code": code,
        "redirect_uri": `${import.meta.env.VITE_FRONT_URL}/authorized`,
        "code_verifier": verifier,
        "grant_type": "authorization_code",
        "client_id": `${import.meta.env.VITE_CLIENT_ID}`,
    });
}

const revokeTokenFromUrlEncodedParams = (token: string) => {
    return new URLSearchParams({
        "client_id": `${import.meta.env.VITE_CLIENT_ID}`,
        "token": token,
        "token_type_hint": "access_token",
    });
}

const initLocalStorageAndRedirectToAuth = () => {
    const cv = codeVerifier();
    localStorage.setItem('codeVerifier', cv);
    const cc = codeChallenge(cv);
    localStorage.setItem('codeChallenge', cc);
    window.location.replace(authorizeUrl(cc))
}

const persistTokens = (jsonTokenResponse: JsonTokens) => {
    localStorage.setItem("id_token", jsonTokenResponse.id_token);
    localStorage.setItem("access_token", jsonTokenResponse.access_token);
}

const clearSession = () => {
    localStorage.removeItem('codeVerifier');
    localStorage.removeItem('codeChallenge');
    localStorage.removeItem('id_token');
    localStorage.removeItem('access_token');
}

export {
    codeChallenge,
    codeChallengeFromLocalStorage,
    codeVerifier,
    codeVerifierFromLocalStorage,
    idTokenFromLocalStorage,
    accessTokenFromLocalStorage,
    tokenFromUrlEncodedParams,
    revokeTokenFromUrlEncodedParams,
    clearSession,
    persistTokens,
    initLocalStorageAndRedirectToAuth
};
