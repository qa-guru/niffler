import axios from "axios";
import {Buffer} from "buffer";

const apiClient = (token) => axios.create({
    baseURL: process.env.REACT_APP_GATEWAY_URL,
    withCredentials: true,
    headers: {
        'Accept': 'application/json',
        'Content-type': 'application/json',
        'Authorization': `Bearer ${token}`,
    }
});

const getData = ({path, onSuccess, onFail, params}) => {
    const token = sessionStorage.getItem('id_token');
    apiClient(token).get(`${path}`, {params})
        .then(res => {
            if (res.status === 200) {
                return res.data;
            } else {
                throw new Error("Error while loading data")
            }
        })
        .then(data => onSuccess(data))
        .catch((err) => {
            onFail(err);
        });
};

const deleteData = ({path, onSuccess, onFail, params}) => {
    const token = sessionStorage.getItem('id_token');
    apiClient(token).delete(`${path}`, {params})
        .then(res => {
            return res.data;
        })
        .then(data => onSuccess(data))
        .catch((err) => {
            onFail(err);
        });
};

const postData = ({path, data, onSuccess, onFail}) => {
    const token = sessionStorage.getItem('id_token');
    apiClient(token).post(`${path}`, data)
        .then(res => {
            if (res.status === 200 || res.status === 201) {
                return res.data;
            } else {
                throw new Error("Entity not created")
            }
        })
        .then(data => onSuccess(data))
        .catch((err) => {
            onFail(err);
        });
};

const patchData = ({path, data, onSuccess, onFail}) => {
    const token = sessionStorage.getItem('id_token');
    apiClient(token).patch(`${path}`, data)
        .then(res => {
            if (res.status === 200) {
                return res.data;
            } else {
                throw new Error("Unsuccessful editing")
            }
        })
        .then(data => onSuccess(data))
        .catch((err) => {
            onFail(err);
        });
};

const AUTH_URL = `${process.env.REACT_APP_AUTH_URL}`;

const authClient = {
    getToken: async (url, data) => {
        const response = await fetch(`${AUTH_URL}/${url}`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-type": "application/x-www-form-urlencoded",
                "Authorization": `Basic ${Buffer.from("client:secret").toString("base64")}`,
            },
            body: data.toString()
        });
        if (!response.ok) {
            throw new Error("Failed loading data");
        }
        return response.json();
    },
    logout: async () => {
        const response = await fetch(`${AUTH_URL}/logout`, {
            method: "GET",
            credentials: "include",
            headers: {
                "Content-type": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("id_token")}`,
            }
        });
        if (!response.ok) {
            throw new Error("Failed logout");
        }
    }
}

export {apiClient, authClient, getData, postData, patchData, deleteData};
