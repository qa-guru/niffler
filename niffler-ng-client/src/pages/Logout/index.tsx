import {Loader} from "../../components/Loader";
import {useEffect} from "react";
import {clearSession, codeChallengeFromLocalStorage, initLocalStorage} from "../../api/authUtils.ts";
import {authorizeUrl} from "../../api/url/auth.ts";

export const LogoutPage = () => {

    useEffect(() => {
        clearSession();
        initLocalStorage();
        window.location.replace(authorizeUrl(codeChallengeFromLocalStorage()));
    }, []);

    return (
        <Loader/>
    )
}
