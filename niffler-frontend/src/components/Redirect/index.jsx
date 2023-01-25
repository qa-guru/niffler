import {useContext, useEffect} from "react";
import {useNavigate, useSearchParams} from "react-router-dom";
import {authClient, getData} from "../../api/api";
import {UserContext} from "../../contexts/UserContext";


export const Redirect = ({}) => {
    const [searchParams] = useSearchParams();
    const { user, setUser } = useContext(UserContext);
    const navigate = useNavigate();

    useEffect(() => {
        if (searchParams?.get('code')) {
            const code = searchParams?.get('code');
            const client = 'client';
            const secret = 'secret';

            const verifier = sessionStorage.getItem('codeVerifier');

            const initialUrl = '/oauth2/token?client_id=client&redirect_uri=http://127.0.0.1:3000/authorized&grant_type=authorization_code';
            const url = `${initialUrl}&code=${code}&code_verifier=${verifier}`;

            authClient({client, secret})
                .post(url)
                .then(res => {
                    return res.data;
                })
                .then( (data) => {
                if (data?.id_token) {
                    sessionStorage.setItem('id_token', data.id_token);
                    getData({
                        path: "/currentUser",
                        onSuccess: (data) => {
                            setUser(data);
                            navigate("/main");
                        },
                        onFail: (err) => {
                            console.log(err);
                            navigate("/login");
                        }
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
            const link = `http://auth-server:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=${codeChallenge}&code_challenge_method=S256`;
            window.location.href = link;
        }
    }, []);

    return <div className="loader"></div>

}
