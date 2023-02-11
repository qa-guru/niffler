import {toast} from "react-toastify";

const showSuccess = (message) => {
    toast.success(message, {
        position: toast.POSITION.TOP_RIGHT,
        autoClose: 3000,
    });
};

const showError = (message) => {
    toast.error(message, {
        position: toast.POSITION.TOP_RIGHT,
        autoClose: 3000,
    });
};

export {showSuccess, showError};
