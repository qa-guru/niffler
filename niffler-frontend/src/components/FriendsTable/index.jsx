import {useEffect, useState} from "react";
import {getData} from "../../api/api";
import {AbstractTable, Controls} from "../AbstractTable";

export const FriendsTable = () => {

    const [friends, setFriends] = useState([]);

    const deleteFriend = ({username}) => {
        setFriends(friends.filter(f => f.username !== username));
    };

    useEffect(() => {
        getData({
                path: "/friends?includePending=false",
                onSuccess: (data) => {
                    console.log(data);
                    setFriends(data);
                },
                onFail: (err) => {
                    console.log(err);
                    setFriends([]);
                }
            }
        );
    }, []);

    return (
        <>
            <AbstractTable data={friends} controls={[Controls.DELETE_FRIEND]} onDelete={deleteFriend}/>
            {friends?.length === 0 && (<div style={{margin: 20}}>There are no friends yet!</div>)}
        </>
    )
}
