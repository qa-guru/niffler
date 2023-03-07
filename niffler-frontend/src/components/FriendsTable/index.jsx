import {useEffect, useState} from "react";
import {getData} from "../../api/api";
import {FriendState} from "../../constants/friendState";
import {AbstractTable} from "../AbstractTable";

export const FriendsTable = () => {

    const [friends, setFriends] = useState([]);
    const [invitations, setInvitations] = useState([]);

    const handleDeleteFriend = ({username}) => {
        setFriends(friends.filter(f => f.username !== username));
    };

    const handleSubmitFriend = ({username}) => {
        const friend = invitations.find(f => f.username === username);
        friend.friendState = FriendState.FRIEND;
        setInvitations(invitations.filter(f => f.username !== username));
        setFriends([...friends, friend]);
    };

    const handleDeclineFriend = ({username}) => {
        setInvitations(invitations.filter(f => f.username !== username));
    };

    useEffect(() => {
        getData({
                path: "/friends?includePending=false",
                onSuccess: (data) => {
                    setFriends(data);
                },
                onFail: (err) => {
                    console.log(err);
                    setFriends([]);
                }
            }
        );
    }, []);

    useEffect(() => {
        getData({
                path: "/invitations",
                onSuccess: (data) => {
                    setInvitations(data);
                },
                onFail: (err) => {
                    console.log(err);
                    setInvitations([]);
                }
            }
        );
    }, []);

    return (
        <>
            <AbstractTable data={[...invitations, ...friends]}
                           onSubmit={handleSubmitFriend}
                           onDecline={handleDeclineFriend}
                           onDelete={handleDeleteFriend}
            />
            {(friends?.length === 0 && invitations.length === 0) && (<div style={{margin: 20}}>There are no friends yet!</div>)}
        </>
    )
}
