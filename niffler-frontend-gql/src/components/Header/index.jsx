import {useQuery} from "@apollo/client";
import {useContext} from "react";
import {Link, useNavigate} from "react-router-dom";
import {Tooltip} from "react-tooltip";
import {getData} from "../../api/api";
import {QUERY_INVITATIONS} from "../../api/graphql/queries";
import {UserContext} from "../../contexts/UserContext";
import {ButtonIcon, IconType} from "../ButtonIcon";

export const Header = () => {
    const navigate = useNavigate()
    const {user} = useContext(UserContext);
    const {data: invitationsData, client} = useQuery(QUERY_INVITATIONS);

    const handleLogout = () => {
        getData({
            path: `${process.env.REACT_APP_AUTH_URL}/logout`,
            onSuccess: () => {
                sessionStorage.clear();
                client.clearStore().then(() => {
                    navigate(0);
                })
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
                    <li className="header__navigation-item"
                        data-tooltip-id="main"
                        data-tooltip-content="Main page"
                    >
                        <Link className="header__link" to={"/main"}>
                            <img className="header__people"
                                 src={"/images/home.svg"} alt="Иконка дома"
                                 width={45}
                                 height={45}
                            />
                        </Link>
                        <Tooltip className="tooltip" id="main"/>
                    </li>
                    <li className="header__navigation-item"
                        data-tooltip-id="friends"
                        data-tooltip-content="Friends"
                    >
                        <Link className="header__link" to={"/friends"}>
                            {invitationsData?.user?.invitations?.length > 0 && (<span className="header__sign"></span>)}
                            <img className="header__people"
                                 src={"/images/friends.svg"} alt="Иконка друзей"
                                 width={48}
                                 height={48}
                            />
                        </Link>
                        <Tooltip className="tooltip" id="friends"/>
                    </li>
                    <li className="header__navigation-item"
                        data-tooltip-id="people"
                        data-tooltip-content="All people"
                    >
                        <Link className="header__link" to={"/people"}>
                            <img className="header__people"
                                 src={"/images/globe.svg"} alt="Иконка глобуса"
                                 width={45}
                                 height={45}
                            />
                        </Link>
                        <Tooltip className="tooltip" id="people"/>
                    </li>
                    <li className="header__navigation-item"
                        data-tooltip-id="profile"
                        data-tooltip-content="Profile">
                        <Link className="header__link" to={"/profile"}>
                            <img className="header__avatar"
                                 src={user?.photo ?? "/images/niffler_avatar.jpeg"} alt="Аватар профиля"
                                 width={65} height={65}/>
                        </Link>
                        <Tooltip className="tooltip" id="profile"/>
                    </li>
                    <li>
                        <div className="header__navigation-item header__logout"
                             data-tooltip-id="logout"
                             data-tooltip-content="Logout">
                            <ButtonIcon iconType={IconType.LOGOUT} onClick={handleLogout}/>
                        </div>
                        <Tooltip className="tooltip" id="logout"/>
                    </li>
                </ul>
            </nav>
        </header>
    )
}
