import {Loader} from "../../components/Loader";
import {useEffect} from "react";
import {clearSession, initLocalStorageAndRedirectToAuth} from "../../api/authUtils.ts";

export const LogoutPage = () => {

    useEffect(() => {
        clearSession();
        initLocalStorageAndRedirectToAuth();
    }, []);

    return (
        <Loader/>
    )
}
