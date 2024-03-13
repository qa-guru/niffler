import {useContext, useEffect} from "react";
import {useNavigate, useSearchParams} from "react-router-dom";
import {authClient} from "../../api/api";
import {UserContext} from "../../contexts/UserContext";


export const Redirect = ({}) => {
    const [searchParams] = useSearchParams();
    const {user, updateUser} = useContext(UserContext);
    const navigate = useNavigate();
    const getTokenFromUrlEncodedParams = (client, code, verifier) => {
        return new URLSearchParams({
            "code": code,
            "redirect_uri": `${process.env.REACT_APP_FRONT_URL}/authorized`,
            "code_verifier": verifier,
            "grant_type": "authorization_code",
            "client_id": client,
        });
    }

    useEffect(() => {
        if (searchParams?.get('code')) {
            const code = searchParams?.get('code');
            const client = 'client';
            const secret = 'secret';
            const verifier = sessionStorage.getItem('codeVerifier');
            const url = 'oauth2/token';

            authClient.getToken(url, getTokenFromUrlEncodedParams(client, code, verifier))
                .then((data) => {
                    if (data?.id_token) {
                        sessionStorage.setItem('id_token', data.id_token);
                        updateUser().then(res => {
                                if (res?.data?.user !== null) {
                                    navigate("/main");
                                } else {
                                    navigate("/login");
                                }
                            }
                        ).catch(err => {
                            console.log(err);
                            navigate("/login")
                        });
                    }
                }).catch((err) => {
                console.log(err);
            })
        }
    }, []);
    useEffect(() => {
        if (!searchParams?.get('code')) {
            const codeChallenge = sessionStorage.getItem('codeChallenge');
            window.location.href = `${process.env.REACT_APP_AUTH_URL}/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=${process.env.REACT_APP_FRONT_URL}/authorized&code_challenge=${codeChallenge}&code_challenge_method=S256`;
        }
    }, []);

    return <div className="loader"></div>

}
