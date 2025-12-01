import {FC} from "react";
import {useSnackBar} from "../../../context/SnackBarContext.tsx";
import {Box, Chip} from "@mui/material";
import {SecondaryButton} from "../../Button";
import {useDialog} from "../../../context/DialogContext.tsx";
import CrossIcon from "../../../assets/icons/ic_cross.svg?react";
import {AddFriendButton} from "../AddFriendButton";
import {AcceptButton} from "../AcceptButton";
import {DeclineButton} from "../DeclineButton";
import {FriendshipAction, FriendshipStatus, useUpdateFriendshipStatusMutation} from "../../../generated/graphql.tsx";

interface ActionButtonsInterface {
    username: string;
    onUpdateCallback: () => void;
    friendshipStatus?: FriendshipStatus | null;
}

export const ActionButtons: FC<ActionButtonsInterface> = ({username, onUpdateCallback, friendshipStatus,}) => {
    const snackbar = useSnackBar();
    const dialog = useDialog();
    const [updateFriendship] = useUpdateFriendshipStatusMutation();

    const handleDeleteFriend = (username: string) => {
        dialog.showDialog({
            title: "Delete friend",
            description: "Do you really want to delete a friend?",
            onSubmit: () => {
                updateFriendship({
                    variables: {
                        input: {
                            username: username,
                            action: FriendshipAction.Delete,
                        }
                    },
                    errorPolicy: "none",
                    onCompleted: () => {
                        onUpdateCallback();
                        snackbar.showSnackBar(`Friend ${username} is deleted`, "success");
                    },
                    onError: (err) => {
                        snackbar.showSnackBar(`Can not delete friend ${username}`, "error");
                        console.error(err);
                    }
                });
            },
            submitTitle: "Delete",
            submitButtonIcon: <CrossIcon/>
        });
    }

    if (!friendshipStatus) {
        return (
            <AddFriendButton username={username} onUpdateCallback={onUpdateCallback}/>
        )
    }

    return (
        <Box sx={{
            margin: 1
        }}>
            {
                friendshipStatus === "FRIEND" && (
                    <SecondaryButton
                        type="button"
                        size="small"
                        onClick={() => handleDeleteFriend(username)}
                    >
                        Unfriend
                    </SecondaryButton>
                )}
            {
                friendshipStatus === "INVITE_SENT" && (
                    <Chip
                        sx={{
                            width: 100
                        }}
                        label="Waiting..."
                    />

                )
            }
            {
                friendshipStatus === "INVITE_RECEIVED" && (
                    <Box sx={{
                        margin: 1
                    }}>
                        <AcceptButton username={username} onUpdateCallback={onUpdateCallback}/>
                        <DeclineButton username={username} onUpdateCallback={onUpdateCallback}/>
                    </Box>
                )
            }
        </Box>
    )
}
