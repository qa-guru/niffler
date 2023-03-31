import {useEffect, useState} from "react";
import {getData} from "../../api/api";
import {FriendState} from "../../constants/friendState";
import {AbstractTable} from "../AbstractTable";

export const PeopleTable = ({}) => {

    const [allUsers, setAllUsers] = useState([]);

    const handleUpdateFriendStatus = ({username, newStatus}) => {
        const uArray = [...allUsers]
        const updatedUser = uArray.find(u => u.username === username);
        updatedUser.friendState = newStatus;
        setAllUsers([...uArray]);
    };
    const handleInvite = ({username}) => {
        handleUpdateFriendStatus({username, newStatus: FriendState.INVITE_SENT})
    };
    const handleDeleteFriend = ({username}) => {
        handleUpdateFriendStatus({username, newStatus: undefined})
    };

    const handleAcceptFriend = ({username}) => {
        handleUpdateFriendStatus({username, newStatus: FriendState.FRIEND})
    }

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


    return (
        <>
            <AbstractTable data={allUsers}
                           onInvite={handleInvite}
                           onDelete={handleDeleteFriend}
                           onSubmit={handleAcceptFriend}
                           onDecline={handleDeleteFriend}
            />
            {allUsers?.length === 0 && (<div style={{margin: 20}}>There are no other users yet!</div>)}
        </>
    )
}
