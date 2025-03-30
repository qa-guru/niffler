import {Loader} from "../../components/Loader";
import {useEffect} from "react";
import {clearSession, initLocalStorageAndRedirectToAuth} from "../../api/authUtils.ts";
import graphqlClient from "../../api/graphqlClient.ts";

export const LogoutPage = () => {

    useEffect(() => {
        clearSession();
        graphqlClient.cache.reset();
        initLocalStorageAndRedirectToAuth();
    }, []);

    return (
        <Loader/>
    )
}
