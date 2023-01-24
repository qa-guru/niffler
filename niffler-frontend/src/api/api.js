import axios from "axios";

const apiClient = (token) => axios.create({
    baseURL: "http://127.0.0.1:8090",
    withCredentials: true,
    headers: {
        'Accept': 'application/json',
        'Content-type': 'application/json',
        'Authorization': `Bearer ${token}`,
    }
});

const getData = ({path, onSuccess, onFail, params}) => {
    const token = sessionStorage.getItem('id_token');
    apiClient(token).get(`${path}`, { params })
        .then(res => {
            return res.data;
        })
        .then(data => onSuccess(data))
        .catch((err) =>{
            onFail(err);
        });
};

const postData = ({path, data, onSuccess, onFail }) => {
    const token = sessionStorage.getItem('id_token');
    apiClient(token).post(`${path}`, data)
        .then(res => {
            return res.data;
        })
        .then(data => onSuccess(data))
        .catch((err) =>{
            onFail(err);
        });
};

export {apiClient, getData, postData};
