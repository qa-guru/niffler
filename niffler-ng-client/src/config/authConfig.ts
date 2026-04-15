const AUTH_URL = `${import.meta.env.VITE_AUTH_URL}`;
const FRONT_URL = `${import.meta.env.VITE_FRONT_URL}`;
const CLIENT_ID = `${import.meta.env.VITE_CLIENT_ID}`;

export const AUTH_CONFIG = {
    clientId: `${CLIENT_ID}`,
    redirectUri: `${FRONT_URL}/authorized`,
    authorizationEndpoint: `${AUTH_URL}/oauth2/authorize`,
    tokenEndpoint: `${AUTH_URL}/oauth2/token`,
    revocationEndpoint: `${AUTH_URL}/oauth2/revoke`,
    logoutEndpoint: `${AUTH_URL}/connect/logout`,
    logoutRedirectUri: `${FRONT_URL}/logout`,
    scope: "openid",
};
