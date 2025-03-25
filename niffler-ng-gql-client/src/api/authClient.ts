import {accessTokenFromLocalStorage, revokeTokenFromUrlEncodedParams} from "./authUtils.ts";
import {revokeAccessTokenUrl, tokenUrl} from "./url/auth.ts";
import {JsonTokens} from "../types/JsonTokens.ts";

export const authClient = {
    getToken: async (data: URLSearchParams): Promise<JsonTokens> => {
        const response = await fetch(tokenUrl(), {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-type": "application/x-www-form-urlencoded",
            },
            body: data.toString()
        });
        if (!response.ok) {
            throw new Error("Failed loading data");
        }
        return response.json();
    },
    revokeAccessToken: async ({onSuccess, onFailure}: { onSuccess: () => void, onFailure: (e: Error) => void }) => {
        const accessToken = accessTokenFromLocalStorage();
        const data = revokeTokenFromUrlEncodedParams(accessToken);
        const logoutResponse = await fetch(revokeAccessTokenUrl(), {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-type": "application/x-www-form-urlencoded",
            },
            body: data.toString()
        });

        if (!logoutResponse.ok) {
            onFailure(new Error("Failed revoke token"))
        } else {
            onSuccess();
        }
    },
}
