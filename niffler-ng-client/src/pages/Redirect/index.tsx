import { useEffect } from "react";
import { initLocalStorageAndRedirectToAuth } from "../../api/authUtils";
import { Loader } from "../../components/Loader";

export const RedirectPage = () => {
    useEffect(() => {
        initLocalStorageAndRedirectToAuth();
    }, []);
    return (
        <Loader/>
    )
}