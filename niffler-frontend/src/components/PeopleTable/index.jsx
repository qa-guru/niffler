import {useEffect, useState} from "react";
import {getData} from "../../api/api";

export const PeopleTable = ({}) => {

    const [allUsers, setAllUsers] = useState([]);

    useEffect(() => {
        getData({
                path: "/users",
                onSuccess: (data) => {
                    setAllUsers(data);
                },
                onFail: (err) => {
                    console.log(err);
                    setAllUsers([]);
                }
            }
        );
    });

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
                    <tr>
                        <td>
                            <img
                                 src={ user.src} alt={`Аватар пользователя ${user.username}`}
                                 width={100} height={100}/>
                        </td>
                        <td>
                            {user.username}
                        </td>
                        <td>{user.firstname} {user.surname}</td>
                        <td>
                            Invite to friends
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            {allUsers?.length === 0 && (<div style={{margin: 20}}>There are no other users yet!</div>)}
        </>
    )
}
