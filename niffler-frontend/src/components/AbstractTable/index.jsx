import {Tooltip} from "react-tooltip";
import {deleteData, postData} from "../../api/api";
import {showError, showSuccess} from "../../toaster/toaster";
import {ButtonIcon, IconType} from "../ButtonIcon";

export const Controls = {
    SEND_INVITATION: "send",
    SUBMIT_FRIEND: "submit",
    DECLINE_FRIEND: "decline",
    DELETE_FRIEND: "delete"
};

const handleAddUserToFriends = (username, onInvite) => {
    postData({
        path: "/addFriend",
        data: {username: username},
        onSuccess: () => {
            showSuccess("Invitation is sent!");
            if(onInvite) {
                onInvite({username});
            }
        },
        onFail: (err) => {
            showError("Can not send invitation!");
            console.error(err);
        }
    });
};

const handleSubmitInvitation = (username, onSubmit) => {
    postData({
        path: "/acceptInvitation",
        data: {username: username},
        onSuccess: () => {
            showSuccess("Invitation is accepted!");
            if(onSubmit) {
                onSubmit({username});
            }
        },
        onFail: (err) => {
            showError("Can not accept invitation!");
            console.error(err);
        }
    });
};

const handleDeclineInvitation = (username, onDecline) => {
    postData({
        path: "/declineInvitation",
        data: {username: username},
        onSuccess: () => {
            showSuccess("Invitation is declined!");
            if(onDecline) {
                onDecline({username});
            }
        },
        onFail: (err) => {
            showError("Can not decline invitation!");
            console.error(err);
        }
    });

}

const handleDeleteFriend = (username, onDelete) => {
    deleteData({
        path: "/removeFriend",
        data: {
            username: username,
        },
        onSuccess: () => {
            showSuccess("Friend is deleted!");
            if(onDelete) {
                onDelete({username});
            }
        },
        onFail: (err) => {
            showError("Can not delete friend!");
            console.error(err);
        }
    });
};

const getControl = (user, control, onDelete, onInvite, onSubmit, onDecline) => {
    switch (control) {
        case Controls.SEND_INVITATION:
            if(user?.pendingFriend === undefined){
                return (
                    <>
                        <div data-tooltip-id="add-friend"
                             data-tooltip-content="Add friend">
                            <ButtonIcon iconType={IconType.ADD_FRIEND}
                                           onClick={() => {
                                               handleAddUserToFriends(user.username, onInvite)}}/>
                        </div>
                        <Tooltip className="tooltip" id="add-friend"/>
                    </>);
            } else if (user?.pendingFriend === false) {
                return <div>You are friends</div>
            }
            return <div>Pending invitation</div>
        case Controls.SUBMIT_FRIEND:
            return (
                <>
                    <div data-tooltip-id="submit-invitation"
                        data-tooltip-content="Submit invitation">
                        <ButtonIcon iconType={IconType.SUBMIT}
                                    onClick={() => {
                                        handleSubmitInvitation(user.username, onSubmit)}}/>
                    </div>
                    <Tooltip className="tooltip" id="submit-invitation"/>
                </>);
        case Controls.DECLINE_FRIEND:
            return (
                <>
                    <div data-tooltip-id="decline-invitation"
                         data-tooltip-content="Decline invitation">
                        <ButtonIcon iconType={IconType.CLOSE}
                               onClick={() => {
                                   handleDeclineInvitation(user.username, onDecline)}}/>
                    </div>
                    <Tooltip className="tooltip" id="decline-invitation"/>
                </>);
        case Controls.DELETE_FRIEND:
            return (
                <>
                    <div data-tooltip-id="remove-friend"
                         data-tooltip-content="Remove friend">
                        <ButtonIcon iconType={IconType.CLOSE}
                                       onClick={() => {
                                           handleDeleteFriend(user.username, onDelete)}}/>
                    </div>
                    <Tooltip className="tooltip" id="remove-friend"/>
                </>);
    }
};
export const AbstractTable = ({data, controls, onDelete, onInvite, onSubmit, onDecline}) => {

    return (
        <>
            <table className="table abstract-table">
                <thead>
                <tr>
                    <th>Avatar</th>
                    <th>Username</th>
                    <th>Name</th>
                    <th>Actions</th>
                </tr>
                </thead>

                <tbody>
                {data?.map((item) => (
                    <tr key={item.username}>
                        <td>
                            <img className="people__user-avatar"
                                 src={ item.photo ?? "/images/niffler_avatar.jpeg"}
                                 alt={`Аватар пользователя ${item.username}`}
                                 width={50} height={50}/>
                        </td>
                        <td>
                            {item.username}
                        </td>
                        <td>{item.firstname} {item.surname}</td>
                        <td>
                            <div className="abstract-table__buttons">
                                {controls.map(control => getControl(item, control, onDelete, onInvite, onSubmit, onDecline))}
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </>
    );
}
