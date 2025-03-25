import {Loader} from "../../components/Loader";
import { useEffect} from "react";
import {clearSession, codeChallengeFromLocalStorage, initLocalStorage} from "../../api/authUtils.ts";
import {authorizeUrl} from "../../api/url/auth.ts";
import graphqlClient from "../../api/graphqlClient.ts";

export const LogoutPage = () => {

    useEffect(() => {
        console.log("LogoutPage");
        clearSession();
        graphqlClient.cache.reset();
        initLocalStorage();
        window.location.replace(authorizeUrl(codeChallengeFromLocalStorage()));
    }, []);

    return (
        <Loader/>
    )
}
