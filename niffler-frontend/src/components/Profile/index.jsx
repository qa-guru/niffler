import {useContext, useState} from "react";
import {postData} from "../../api/api";
import {useLoadedData} from "../../api/hooks";
import {UserContext} from "../../contexts/UserContext";
import {Button} from "../Button";
import {FormInput} from "../FormInput";
import {FormSelect} from "../FormSelect";
import {Header} from "../Header";
import {ProfileAvatar} from "../ProfileAvatar";

export const Profile = ({showSuccess, showError}) => {

    const {user, setUser} = useContext(UserContext);
    const [userData, setUserData] = useState({
        firstname: user?.firstname ?? "",
        surname: user?.surname ?? "",
        currency: {value: user.currency, label: user.currency}
        });
    const [currencies, setCurrencies] = useState([]);

    useLoadedData({
        path: "/allCurrencies",
        onSuccess: (data) => {
            setCurrencies(Array.from(data.map((v) => {
                return {value: v?.currency, label: v?.currency}
            })));
        },
        onFail: (err) => {
            console.log(err);
        }
    });

    const handleChangeProfile = (e) => {
        e.preventDefault();
        const dataToSend = {...userData, currency: userData.currency?.value};
        postData({
            path:"/updateUserInfo",
            data: dataToSend,
            onSuccess: (data) => {
               setUser(data);
               showSuccess("Profile updated!");
            },
            onFail: (err) => {
                console.log(err);
                showError("Error while updating profile");
            }
        });
    };

    const handleProfileAvatarChange = (e) => {
    }


    return (
    <div className={"main-container"}>
        <Header/>
        <main className={"main"}>
            <div className={"profile-content"}>
                <section className="main-content__section">
                    <div className="main-content__section-avatar">
                        <form onSubmit={handleChangeProfile}>
                        <ProfileAvatar username={user?.username}
                                       value={userData?.photo}
                                       handleChangeValue={handleProfileAvatarChange}/>
                            <div className="profile__info-container">
                                    <FormInput placeholder={"Set your name"}
                                               value={userData.firstname}
                                               label="Name"
                                               fieldName={"firstname"}
                                               max={30}
                                               handleChangeValue={(data) => setUserData({...userData, firstname: data.target.value})}
                                    />
                                    <FormInput label="Surname"
                                               fieldName={"surname"}
                                               value={userData.surname}
                                               placeholder={"Set your surname"}
                                               max={50}
                                               handleChangeValue={(data) => {
                                                   setUserData({...userData, surname: data.target.value})
                                               }
                                    }
                                    />
                                    <div style={{width: 350}}>
                                        <FormSelect options={currencies}
                                                    placeholder="Select currency"
                                                    value={userData?.currency}
                                                    label="Currency"
                                                    onChange={(currency) => {
                                                        setUserData({...userData, currency})}}
                                        />
                                    </div>
                            </div>
                            <Button buttonText={"Submit"}/>
                        </form>
                    </div>
                </section>
            </div>
        </main>
        <footer className={"footer"}>
            Study project for QA Automation Advanced. 2023
        </footer>
    </div>
    );
}
