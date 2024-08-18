import {clearSession, initLocalStorageAndRedirectToAuth} from "./authUtils.ts";
import {Category} from "../types/Category.ts";
import {User} from "../types/User.ts";
import {RequestHandler} from "../types/RequestHandler.ts";
import {Currency} from "../types/Currency.ts";
import {Spending} from "../types/Spending.ts";
import {Statistic} from "../types/Statistic.ts";
import {ApiError, isApiError, isCommonError} from "../types/Error.ts";
import {Session} from "../types/Session.ts";
import {Void} from "../types/Void.ts";

const BASE_URL = `${import.meta.env.VITE_API_URL}/api`;
const DEFAULT_ABORT_TIMEOUT = 5000;

interface RequestOptions {
    method: "GET" | "POST" | "DELETE" | "PUT" | "PATCH",
    headers?: HeadersInit,
    body?: BodyInit | null,
    timeout?: number,
}


export const apiClient = {
    getSession: async ({onSuccess, onFailure}: RequestHandler<Session>) => {
        await makeRequest("/session/current", {
            onSuccess,
            onFailure,
        },);
    },

    getProfile: async ({onSuccess, onFailure}: RequestHandler<User>) => {
        await makeRequest("/users/current", {
            onSuccess,
            onFailure,
        });
    },

    updateProfile: async (user: User, {onSuccess, onFailure}: RequestHandler<User>) => {
        await makeRequest("/users/update",
            {
                onSuccess,
                onFailure,
            },
            {
                method: "POST",
                body: JSON.stringify(user),
            });
    },

    getCategories: async ({onSuccess, onFailure}: RequestHandler<Category[]>, excludeArchived: boolean = false) => {
        await makeRequest(`/categories/all?excludeArchived=${excludeArchived}`, {
            onSuccess,
            onFailure,
        });
    },

    addCategory: async (name: string, {onSuccess, onFailure}: RequestHandler<Category>) => {
        await makeRequest("/categories/add",
            {
                onSuccess,
                onFailure,
            },
            {
                method: "POST",
                body: JSON.stringify({
                    name: name,
                }),
            });
    },

    editCategory: async (category: Category, {onSuccess, onFailure}: RequestHandler<Category>) => {
        await makeRequest("/categories/update",
            {
                onSuccess,
                onFailure,
            },
            {
                method: "PATCH",
                body: JSON.stringify(
                    category
                ),
            });
    },

    getCurrencies: async ({onSuccess, onFailure}: RequestHandler<Currency[]>) => {
        await makeRequest("/currencies/all", {
            onSuccess,
            onFailure,
        });
    },

    getSpends: async (searchQuery: string, page: number, {
        onSuccess,
        onFailure
    }: RequestHandler<Pageable<Spending>>, filterPeriod: string, filterCurrency: string) => {
        await makeRequest(
            `/v2/spends/all?page=${page}&searchQuery=${searchQuery}&filterCurrency=${filterCurrency}&filterPeriod=${filterPeriod}`,
            {
                onSuccess,
                onFailure,
            },
        );
    },

    getSpend: async (id: string, {onSuccess, onFailure}: RequestHandler<Spending>) => {
        await makeRequest(`/spends/${id}`, {
            onSuccess,
            onFailure,
        });
    },

    addSpend: async (spending: any, {onSuccess, onFailure}: RequestHandler<Spending>) => {
        await makeRequest("/spends/add",
            {
                onSuccess,
                onFailure,
            },
            {
                method: "POST",
                body: JSON.stringify(spending),
            });
    },

    editSpend: async (spending: any, {onSuccess, onFailure}: RequestHandler<Spending>) => {
        await makeRequest("/spends/edit",
            {
                onSuccess,
                onFailure,
            },
            {
                method: "PATCH",
                body: JSON.stringify(spending),
            });
    },

    deleteSpends: async (spendIds: string[], {onSuccess, onFailure}: RequestHandler<Spending>) => {
        await makeRequest(`/spends/remove?ids=${spendIds.join(",")}`,
            {
                onSuccess,
                onFailure,
            },
            {
                method: "DELETE",
            });
    },

    getStat: async ({onSuccess, onFailure}: RequestHandler<Statistic>, filterPeriod: string, currency: string) => {
        await makeRequest(`/v2/stat/total?filterCurrency=${currency}&statCurrency=${currency}&filterPeriod=${filterPeriod}`,
            {
                onSuccess,
                onFailure,
            },);
    },

    getAllPeople: async (searchQuery: string, page: number, {onSuccess, onFailure}: RequestHandler<Pageable<User>>) => {
        await makeRequest(`/v2/users/all?page=${page}&searchQuery=${searchQuery}&sort=username,ASC`, {
            onSuccess,
            onFailure,
        });
    },

    getFriends: async (searchQuery: string, page: number, {onSuccess, onFailure}: RequestHandler<Pageable<User>>) => {
        await makeRequest(`/v2/friends/all?page=${page}&searchQuery=${searchQuery}&sort=username,ASC`,
            {
                onSuccess,
                onFailure,
            });

    },

    sendInvitation: async (username: string, {onSuccess, onFailure}: RequestHandler<User>) => {
        await makeRequest("/invitations/send",
            {
                onSuccess,
                onFailure,
            },
            {
                method: "POST",
                body: JSON.stringify({
                    username,
                }),
            });
    },

    acceptInvitation: async (username: string, {onSuccess, onFailure}: RequestHandler<User>) => {
        await makeRequest("/invitations/accept",
            {
                onSuccess,
                onFailure,
            },
            {
                method: "POST",
                body: JSON.stringify({
                    username,
                }),
            });
    },

    declineInvitation: async (username: string, {onSuccess, onFailure}: RequestHandler<User>) => {
        await makeRequest("/invitations/decline",
            {
                onSuccess,
                onFailure,
            },
            {
                method: "POST",
                body: JSON.stringify({
                    username,
                }),
            });
    },

    deleteFriend: async (username: string, {onSuccess, onFailure}: RequestHandler<Void>) => {
        await makeRequest(
            `/friends/remove?username=${username}`,
            {
                onSuccess,
                onFailure,
            },
            {
                method: "DELETE",
            });
    },
}

async function makeRequest<T>(path: string, {onSuccess, onFailure}: RequestHandler<T>, options?: RequestOptions) {
    const url = `${BASE_URL}${path}`;
    const controller = new AbortController();
    const {signal} = controller;

    const baseHeaders = {
        "Content-Type": "application/json",
        "Accept": "application/json",
    };

    const config: RequestInit = {
        ...options,
        headers: {
            ...baseHeaders,
            ...options?.headers,
        },
        credentials: "include",
        signal,
    };

    const token = localStorage.getItem("id_token");
    if (token) {
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

        if (response.status !== 204 && options?.method !== "DELETE") {
            const data = await response.json();
            if (!response.ok) {
                throw new ApiError(data.detail, data.status);
            }
            onSuccess(data);
        }

        if (options?.method === "DELETE") {
            if (!response.ok) {
                throw new Error("Failed DELETE request");
            }
            onSuccess(<Void>{} as T);
        }
    } catch (error) {
        if (isCommonError(error)) {
            onFailure(error);
        } else if (isApiError(error)) {
            onFailure(error);
        } else {
            onFailure(new Error("An unknown error occurred"));
        }
    }
}