import {useEffect, useState} from "react";
import {getData, postData} from "../../api/api";
import {showError, showSuccess} from "../../toaster/toaster";
import {ButtonIcon, IconType} from "../ButtonIcon";

export const PeopleTable = ({}) => {

    const [allUsers, setAllUsers] = useState([]);

    useEffect(() => {
        getData({
                path: "/allUsers",
                onSuccess: (data) => {
                    console.log(data);
                    setAllUsers(data);
                },
                onFail: (err) => {
                    console.log(err);
                    setAllUsers([]);
                }
            }
        );
    }, []);

    const handleAddUserToFriends = (username) => {
        console.log(username);
        postData({
            path: `/addFriend?username=${username}`,
            onSuccess: (data) => {
                console.log(data);
                showSuccess("Invitation is sent!");
            },
            onFail: (err) => {
                showError("Can not send invitation!");
                console.error(err);
            }
        });
    }

    return (
        <>
            <table className="table people-table">
                <thead>
                <tr>
                    <th>Avatar</th>
                    <th>Username</th>
                    <th>Name</th>
                    <th>Actions</th>
                </tr>
                </thead>

                <tbody>
                {allUsers?.map((user) => (
                    <tr key={user.username}>
                        <td>
                            <img className="people__user-avatar"
                                 src={ user.photo ?? "/images/niffler_avatar.jpeg"}
                                 alt={`Аватар пользователя ${user.username}`}
                                 width={50} height={50}/>
                        </td>
                        <td>
                            {user.username}
                        </td>
                        <td>{user.firstname} {user.surname}</td>
                        <td>
                            {user.pending === undefined ?
                                (
                                <ButtonIcon iconType={IconType.ADD_FRIEND} onClick={() => {
                                handleAddUserToFriends(user.username)
                                }}/>) :  (<div>Invitation is sent</div>)}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            {allUsers?.length === 0 && (<div style={{margin: 20}}>There are no other users yet!</div>)}
        </>
    )
}
