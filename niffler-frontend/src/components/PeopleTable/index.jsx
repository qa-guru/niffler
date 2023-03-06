import {useEffect, useState} from "react";
import {getData} from "../../api/api";
import {AbstractTable, Controls} from "../AbstractTable";

export const PeopleTable = ({}) => {

    const [allUsers, setAllUsers] = useState([]);

    const handleInvite = ({username}) => {
        const uArray = [...allUsers]
        const updatedUser = uArray.find(u => u.username === username);
        updatedUser.pendingFriend = true;
        setAllUsers([...uArray]);
    };

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
            <AbstractTable data={allUsers} controls={[Controls.SEND_INVITATION]} onInvite={handleInvite}/>
            {allUsers?.length === 0 && (<div style={{margin: 20}}>There are no other users yet!</div>)}
        </>
    )
}
