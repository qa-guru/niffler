import {useContext} from "react";
import {Link, Navigate} from "react-router-dom";
import {generateCodeChallenge, generateCodeVerifier} from "../../api/utils";
import {UserContext} from "../../contexts/UserContext";


export const LoginPage = () => {

    const verifier = generateCodeVerifier();
    sessionStorage.setItem('codeVerifier', verifier);
    const codeChallenge = generateCodeChallenge();
    sessionStorage.setItem('codeChallenge', codeChallenge);
    const {user} = useContext(UserContext);

    return (
        <>
            {user ? <Navigate to={"/main"}/> : (
                <div className={"main-page__container"}>
                    <div className={"form"}>
                        <h1 className={"main__header"}>Welcome to magic journey with Niffler. The coin keeper</h1>
                        <img className={"main__logo"} src="/images/niffler-logo.png" width={150}
                             height={150} alt="Logo Niffler"></img>
                        <div className={"main__links"}>
                            <Link className={"main__link"} to={'/redirect'}>Login</Link>
                            <a className={"main__link"} href={`${process.env.REACT_APP_AUTH_URL}/register`}>Register</a>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}
