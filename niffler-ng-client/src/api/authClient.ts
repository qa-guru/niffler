const BASE_URL = `${import.meta.env.VITE_AUTH_URL}`;

export const authClient = {
    getToken: async (url: string, data: URLSearchParams) => {
        const response = await fetch(`${BASE_URL}/${url}`, {
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
    logout: async ({onSuccess, onFailure}: { onSuccess: () => void, onFailure: (e: Error) => void }) => {
        const response = await fetch(`${BASE_URL}/logout`, {
            method: "GET",
            credentials: "include",
            headers: {
                "Content-type": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("id_token")}`,
            }
        });
        if (!response.ok) {
            onFailure(new Error("Failed logout"))
        } else {
            onSuccess();
        }
    }
}
