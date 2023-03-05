import {useContext} from "react";
import {Link, useLocation} from "react-router-dom";
import {getData} from "../../api/api";
import {UserContext} from "../../contexts/UserContext";
import {ButtonIcon, IconType} from "../ButtonIcon";

export const Header = () => {
    const location = useLocation();
    const { user, setUser } = useContext(UserContext);

    const handleLogout = () => {
        getData({
            path: `${process.env.REACT_APP_AUTH_URL}/logout`,
            onSuccess: () => {
                sessionStorage.clear();
                setUser(null);
                location.pathname = "/login";
            },
            onFail: (err) => {
                console.log(err);
            }
        });
    }
    return (
        <header className="header">
            <div className="header__group">
                <img className="header__logo" src="/images/niffler-logo.png" width={70} height={70}
                     alt="Logo Niffler"/>
                <h1 className="header__title">Niffler. The coin keeper.</h1>
            </div>
            <nav className="header__navigation">
                <ul>
                    <li className="header__navigation-item">
                        <Link className="header__link" to={"/main"}>
                            <img className="header__people"
                                 src={"/images/home.svg"} alt="Иконка дома"
                                 width={60}
                                 height={60}
                            />
                        </Link>
                    </li>
                    <li className="header__navigation-item">
                        <Link className="header__link" to={"/friends"}>
                            <img className="header__people"
                                 src={"/images/friends.svg"} alt="Иконка друзей"
                                 width={45}
                                 height={45}
                            />
                        </Link>
                    </li>
                    <li className="header__navigation-item">
                        <Link className="header__link" to={"/people"}>
                            <img className="header__people"
                                 src={"/images/globe.svg"} alt="Иконка глобуса"
                                 width={45}
                                 height={45}
                            />
                        </Link>
                    </li>
                    <li className="header__navigation-item">
                        <Link className="header__link" to={"/profile"}>
                            <img className="header__avatar"
                                 src={user?.photo ?? "/images/niffler_avatar.jpeg"} alt="Аватар профиля"
                                 width={45} height={45}/>
                        </Link>
                    </li>
                    <li>
                        <div className="header__logout">
                            <ButtonIcon iconType={IconType.LOGOUT} onClick={handleLogout}/>
                        </div>
                    </li>
                </ul>
            </nav>
        </header>
    )
}
