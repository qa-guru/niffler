import { accessTokenFromLocalStorage, bearerToken, revokeTokenFromUrlEncodedParams } from "./authUtils.ts";
import { registerPasskeyOptionsUrl, registerPasskeyUrl, revokeAccessTokenUrl, tokenUrl } from "./url/auth.ts";
import { JsonTokens } from "../types/JsonTokens.ts";
import { RegisterPasskeyPayload } from "../types/RegisterPasskeyPayload.ts";
import { RequestHandler } from "../types/RequestHandler.ts";
import { ApiError } from "../types/Error.ts";

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

    registerPasskeyOptions: async (csrf: string, { onSuccess, onFailure }: RequestHandler<any>): Promise<any> => {
        const token: string = await bearerToken();
        const response = await fetch(registerPasskeyOptionsUrl(), {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-type": "application/json",
                "X-XSRF-TOKEN": csrf,
                "Authorization": token
            },
        });
        if (!response.ok || response.headers.get("content-type")?.includes("text/html")) {
            onFailure(new ApiError("Failed to request webauth options", response.status));
        }
        return onSuccess(response.json());
    },

    registerPasskey: async (payload: RegisterPasskeyPayload, csrf: string, { onSuccess, onFailure }: RequestHandler<any>): Promise<any> => {
        const token: string = await bearerToken();
        const response = await fetch(registerPasskeyUrl(), {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-type": "application/json",
                "X-XSRF-TOKEN": csrf,
                "Authorization": token
            },
            body: JSON.stringify(payload)
        });
        if (!response.ok || response.headers.get("content-type")?.includes("text/html")) {
            onFailure(new ApiError("Failed to request webauth options", response.status));
        }
        return onSuccess(response.json());
    },

    revokeAccessToken: async ({ onSuccess, onFailure }: { onSuccess: () => void, onFailure: (e: Error) => void }) => {
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
