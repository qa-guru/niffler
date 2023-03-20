import {useMutation} from "@apollo/client";
import {Tooltip} from "react-tooltip";
import {
    ACCEPT_INVITATION_MUTATION,
    ADD_FRIEND_MUTATION,
    DECLINE_INVITATION_MUTATION,
    REMOVE_FRIEND_MUTATION
} from "../../api/graphql/mutations";
import {FriendState} from "../../constants/friendState";
import {showError, showSuccess} from "../../toaster/toaster";
import {ButtonIcon, IconType} from "../ButtonIcon";


const getControls = ({user, addFriend, declineInvitation, acceptInvitation, removeFriend}) => {
    const friendState = user.friendState;


    if (friendState === FriendState.INVITE_SENT) {
        return (<div>Pending invitation</div>);
    } else if (friendState === FriendState.FRIEND) {
        return (
            <>
                <div>You are friends</div>
                <>
                    <div data-tooltip-id="remove-friend"
                         data-tooltip-content="Remove friend">
                        <ButtonIcon iconType={IconType.CLOSE}
                                    onClick={() => {
                                        removeFriend({
                                            variables: {
                                                username: user.username
                                            }
                                        }).then(() => showError("Friend is removed!"));
                                    }}/>
                    </div>
                    <Tooltip className="tooltip" id="remove-friend"/>
                </>
            </>);
    } else if (friendState === FriendState.INVITE_RECEIVED) {
        return (
            <>
                <>
                    <div data-tooltip-id="submit-invitation"
                         data-tooltip-content="Submit invitation">
                        <ButtonIcon iconType={IconType.SUBMIT}
                                    onClick={() => {
                                        acceptInvitation({
                                            variables: {
                                                username: user.username
                                            }
                                        }).then(() => showSuccess("Invitation submitted!"));
                                    }}/>
                    </div>
                    <Tooltip className="tooltip" id="submit-invitation"/>
                </>
                <>
                    <div data-tooltip-id="decline-invitation"
                         data-tooltip-content="Decline invitation">
                        <ButtonIcon iconType={IconType.CLOSE}
                                    onClick={() => {
                                        declineInvitation({
                                            variables: {
                                                username: user.username
                                            }
                                        }).then(() => showError("Invitation declined!"));
                                    }}/>
                    </div>
                    <Tooltip className="tooltip" id="decline-invitation"/>
                </>
            </>
        )
    } else {
        return (
            <>
                <div data-tooltip-id="add-friend"
                     data-tooltip-content="Add friend">
                    <ButtonIcon iconType={IconType.ADD_FRIEND}
                                onClick={() => {
                                    addFriend({
                                        variables: {
                                            username: user.username
                                        }
                                    }).then(() => showSuccess("Invitation sent!"));
                                }}/>
                </div>
                <Tooltip className="tooltip" id="add-friend"/>
            </>
        )
    }
};
export const AbstractTable = ({data, onDelete, onDecline}) => {
    const [addFriend] = useMutation(ADD_FRIEND_MUTATION);
    const [declineInvitation] = useMutation(DECLINE_INVITATION_MUTATION);
    const [acceptInvitation] = useMutation(ACCEPT_INVITATION_MUTATION);
    const [removeFriend] = useMutation(REMOVE_FRIEND_MUTATION);

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
                                 src={item.photo ?? "/images/niffler_avatar.jpeg"}
                                 alt={`Аватар пользователя ${item.username}`}
                                 width={50} height={50}/>
                        </td>
                        <td>
                            {item.username}
                        </td>
                        <td>{item.firstname} {item.surname}</td>
                        <td>
                            <div className="abstract-table__buttons">
                                {getControls({
                                    user: item,
                                    addFriend,
                                    declineInvitation,
                                    acceptInvitation,
                                    removeFriend
                                })}
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </>
    );
}
