import {useEffect} from "react";
import {Loader} from "../../components/Loader"
import {useNavigate, useSearchParams} from "react-router-dom";
import {codeVerifierFromLocalStorage, persistTokens, tokenFromUrlEncodedParams} from "../../api/authUtils";
import {authClient} from "../../api/authClient";

export const AuthorizedPage = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const getToken = async (data: URLSearchParams) => {
        const res = await authClient.getToken(data);
        if (res?.id_token) {
            persistTokens(res);
            setTimeout(async () => {
                navigate("/main", {replace: true});
            }, 500);
        } else {
            console.log("Не удалось получить токен");
            navigate("/main");
        }
    };

    useEffect(() => {
        const code = searchParams?.get("code");
        const verifier = codeVerifierFromLocalStorage();
        if (code && verifier) {
            const data = tokenFromUrlEncodedParams(code, verifier);
            getToken(data);
        } else {
            console.log("Can not login to Cabinet");
            navigate("/");
        }
    }, []);

    return (
        <Loader/>
    )
}
