import {clearSession, initLocalStorageAndRedirectToAuth} from "./authUtils.ts";
import {Category} from "../types/Category.ts";
import {User} from "../types/User.ts";
import {RequestHandler} from "../types/RequestHandler.ts";
import {Currency} from "../types/Currency.ts";
import {Spending} from "../types/Spending.ts";
import {Statistic} from "../types/Statistic.ts";

const BASE_URL = `${import.meta.env.VITE_API_URL}/api`;
const DEFAULT_ABORT_TIMEOUT = 5000;

interface RequestOptions {
    method: "GET" | "POST" | "DELETE" | "PUT" | "PATCH",
    headers?: HeadersInit,
    body?: BodyInit | null,
    timeout?: number,
}

export const apiClient = {
    getSession: async() => {
        return await makeRequest("/session/current");
    },

    getProfile: async({onSuccess, onFailure}: RequestHandler<User>) => {
        try {
            const result = await makeRequest("/users/current");
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    updateProfile: async(user: User, {onSuccess, onFailure}: RequestHandler<User>) => {
        try {
            const result = await makeRequest("/users/update", {
                method: "POST",
                body: JSON.stringify(user),
            });
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    getCategories: async({ onSuccess, onFailure }: RequestHandler<Category[]>) => {
        try {
            const result = await makeRequest("/categories/all");
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    addCategory: async (name: string, {onSuccess, onFailure}: RequestHandler<Category>) => {
        try {
            const result = await makeRequest("/categories/add", {
                method: "POST",
                body: JSON.stringify({
                    name: name,
                }),
            });
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    editCategory: async (name: string, {onSuccess, onFailure}: RequestHandler<Category>) => {
        try {
            const result = await makeRequest("/categories/edit", {
                method: "PATCH",
                body: JSON.stringify({
                    name: name,
                }),
            });
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    getCurrencies: async({ onSuccess, onFailure }: RequestHandler<Currency[]>) => {
        try {
            const result = await makeRequest("/currencies/all");
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    getSpends: async({ onSuccess, onFailure }: RequestHandler<Spending[]>) => {
        try {
            const result = await makeRequest("/v2/spends/all");
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    getSpend: async(id: string, { onSuccess, onFailure }: RequestHandler<Spending>) => {
        try {
            const result = await makeRequest(`/spends/${id}`);
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    addSpend: async(spending: any, { onSuccess, onFailure }: RequestHandler<Spending> ) => {
        try {
            const result = await makeRequest("/spends/add", {
                method: "POST",
                body: JSON.stringify(spending),
            });
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    editSpend: async(spending: any, { onSuccess, onFailure }: RequestHandler<Spending> ) => {
        try {
            const result = await makeRequest("/spends/edit", {
                method: "PATCH",
                body: JSON.stringify(spending),
            });
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    getStat: async({ onSuccess, onFailure }: RequestHandler<Statistic>) => {
        try {
            const result = await makeRequest("/v2/stat/total");
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    getAllPeople: async(searchQuery: string, page: number, { onSuccess, onFailure }: RequestHandler<Pageable<User>>) => {
        try {
            const result = await makeRequest(`/v2/users/all?page=${page}&searchQuery=${searchQuery}`);
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    getFriends: async(searchQuery: string, page: number, { onSuccess, onFailure }: RequestHandler<Pageable<User>>) => {
        try {
            const result = await makeRequest(`/v2/friends/all?page=${page}&searchQuery=${searchQuery}`);
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    sendInvitation: async(username: string, { onSuccess, onFailure }: RequestHandler<any> ) => {
        try {
            const result = await makeRequest("/invitations/send", {
                method: "POST",
                body: JSON.stringify({
                    username,
                }),
            });
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    acceptInvitation: async(username: string, { onSuccess, onFailure }: RequestHandler<any> ) => {
        try {
            const result = await makeRequest("/invitations/accept", {
                method: "POST",
                body: JSON.stringify({
                    username,
                }),
            });
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    declineInvitation: async(username: string, { onSuccess, onFailure }: RequestHandler<any> ) => {
        try {
            const result = await makeRequest("/invitations/decline", {
                method: "POST",
                body: JSON.stringify({
                    username,
                }),
            });
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },

    deleteFriend: async(username: string, { onSuccess, onFailure }: RequestHandler<any> ) => {
        try {
            const result = await makeRequest(`/friends/remove?username=${username}`, {
                method: "DELETE",
            });
            onSuccess(result);
        } catch(e: Error) {
            onFailure(e)
        }
    },
}

const makeRequest = async( path: string, options?: RequestOptions) => {
    const url = `${BASE_URL}${path}`;
    const controller = new AbortController();
    const {signal} = controller;

    const baseHeaders = {
        "Content-Type": "application/json",
        "Accept": "application/json",
    };

    const config: RequestInit  = {
        ...options,
        headers: {
            ...baseHeaders,
            ...options?.headers,
        },
        credentials: "include",
        signal,
    };

    const token= localStorage.getItem("id_token");
    if(token) {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        config.headers["Authorization"] = `Bearer ${token}`;
    }

    const timeout = setTimeout(() => controller.abort(), options?.timeout || DEFAULT_ABORT_TIMEOUT);

    try {
        const response = await fetch(url, config);
        clearTimeout(timeout);

        if (response.status === 401) {
            clearSession();
            initLocalStorageAndRedirectToAuth();
        }

        if(response.status !== 204 && options?.method !== "DELETE") {
            const data = await response.json();
            if(!response.ok) {
                throw new Error(`${data?.detail}`);
            }
            return data;
        }
    } catch (error) {
        if(error.name === "AbortError") {
            console.error("Request aborted");
            throw error;
        } else {
            console.error("API request error");
            throw error;
        }
    }

}