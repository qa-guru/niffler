import {useContext} from "react";
import {Link, useNavigate} from "react-router-dom";
import {useLoadedData} from "../../api/hooks";
import {generateCodeChallenge, generateCodeVerifier} from "../../api/utils";
import {UserContext} from "../../contexts/UserContext";


export const LoginPage = () => {

    const verifier = generateCodeVerifier();
    sessionStorage.setItem('codeVerifier', verifier);
    const codeChallenge = generateCodeChallenge();
    sessionStorage.setItem('codeChallenge', codeChallenge);
    const { user, setUser } = useContext(UserContext);
    const navigate = useNavigate();

    useLoadedData({
        path: "/currentUser",
        onSuccess: (user) => {
            setUser(user);
            navigate("/main");
        },
        onFail: (err) => {
            console.log(err);
        }
    })

    return (
        <>
            <div className={"main-page__container"}>
                <div className={"form"}>
                    <h1 className={"main__header"}>Welcome to magic journey with Niffler. The coin keeper</h1>
                    <img className={"main__logo"} src="/images/niffler-logo.png" width={150}
                         height={150} alt="Logo Niffler"></img>
                    <div className={"main__links"}>
                        <Link className={"main__link"} to={'/redirect'}>Login</Link>
                        <a className={"main__link"} href={`${process.env.REACT_APP_AUTH_URL}/register`} >Register</a>
                    </div>
                </div>
            </div>
        </>
    );
}
