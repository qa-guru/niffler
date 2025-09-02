const AUTH_URL = `${import.meta.env.VITE_AUTH_URL}`;
const FRONT_URL = `${import.meta.env.VITE_FRONT_URL}`;
const CLIENT_ID = `${import.meta.env.VITE_CLIENT_ID}`;

const authorizeUrl = (codeChallenge: string) => {
    return `${AUTH_URL}/oauth2/authorize?response_type=code&client_id=${CLIENT_ID}&scope=openid&redirect_uri=${FRONT_URL}/authorized&code_challenge=${codeChallenge}&code_challenge_method=S256`;
}

const tokenUrl = () => {
    return `${AUTH_URL}/oauth2/token`;
}

const logoutUrl = (token: string) => {
    return `${AUTH_URL}/connect/logout?id_token_hint=${token}&post_logout_redirect_uri=${FRONT_URL}/logout`;
}

const revokeAccessTokenUrl = () => {
    return `${AUTH_URL}/oauth2/revoke`;
}

const registerPasskeyOptionsUrl = () => {
    return `${AUTH_URL}/webauthn/register/options`;
}

const registerPasskeyUrl = () => {
    return `${AUTH_URL}/webauthn/register`;
}

export {
    authorizeUrl,
    logoutUrl,
    tokenUrl,
    revokeAccessTokenUrl,
    registerPasskeyOptionsUrl,
    registerPasskeyUrl,
};
