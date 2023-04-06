import {useMutation, useQuery} from "@apollo/client";
import {useContext, useMemo, useState} from "react";
import {UPDATE_USER_INFO_MUTATION} from "../../api/graphql/mutations";
import {QUERY_ALL_CURRENCIES} from "../../api/graphql/queries";
import {UserContext} from "../../contexts/UserContext";
import {showError, showSuccess} from "../../toaster/toaster";
import {Button} from "../Button";
import {Categories} from "../Categories";
import {FormInput} from "../FormInput";
import {FormSelect} from "../FormSelect";
import {PageContainer} from "../PageContainer";
import {ProfileAvatar} from "../ProfileAvatar";

export const Profile = () => {

    const {user} = useContext(UserContext);
    const [userData, setUserData] = useState({
        firstname: user?.firstname ?? "",
        surname: user?.surname ?? "",
        currency: {value: user.currency, label: user.currency},
        photo: user?.photo ?? null,
    });

    const {data: currenciesData, loading: currenciesLoading, error: currenciesError} = useQuery(QUERY_ALL_CURRENCIES);
    const currencies = useMemo(() => {
        if (currenciesLoading || currenciesError) return [];
        const result = currenciesData.currencies.map((v) => {
            return {value: v?.currency, label: v?.currency}
        });
        return result;
    }, [currenciesLoading, currenciesError, currenciesData]);

    const [updateUserInfo] = useMutation(UPDATE_USER_INFO_MUTATION);

    const handleChangeProfile = (e) => {
        e.preventDefault();
        updateUserInfo(
            {
                variables:
                    {
                        input: {
                            ...userData, currency: userData.currency?.value
                        }
                    }
            }).then(res => {
            if (res?.data?.updateUserInfo !== null) {
                showSuccess("Profile successfully updated")
            } else {
                showError("Can not update Profile")
            }
        }).catch((err) => {
            showError("Can not update Profile");
            console.log(err);
        });
    };

    const handleProfileAvatarChange = (selectedFile) => {
        if (selectedFile && selectedFile !== userData.photo) {
            const reader = new FileReader();
            reader.onload = function () {
                const result = reader.result;
                setUserData({...userData, photo: result})
            }
            reader.readAsDataURL(selectedFile);
        }
    }


    return (
        <PageContainer>
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
                                           handleChangeValue={(data) => setUserData({
                                               ...userData,
                                               firstname: data.target.value
                                           })}
                                />
                                <FormInput label="Surname"
                                           fieldName={"surname"}
                                           value={userData.surname}
                                           placeholder={"Set your surname"}
                                           max={50}
                                           handleChangeValue={(data) => setUserData({
                                               ...userData,
                                               surname: data.target.value
                                           })}
                                />
                                <div style={{width: 350}}>
                                    <FormSelect options={currencies}
                                                placeholder="Select currency"
                                                value={userData?.currency}
                                                label="Currency"
                                                onChange={(currency) => {
                                                    setUserData({...userData, currency})
                                                }}
                                    />
                                </div>
                            </div>
                            <Button type="submit" buttonText={"Submit"}/>
                        </form>
                    </div>
                </section>
                <Categories/>
            </div>
        </PageContainer>
    );
}
