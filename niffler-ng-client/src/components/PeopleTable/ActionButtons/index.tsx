import {FC} from "react";
import {useSnackBar} from "../../../context/SnackBarContext.tsx";
import {Box, Chip} from "@mui/material";
import {SecondaryButton} from "../../Button";
import {apiClient} from "../../../api/apiClient.ts";
import {useDialog} from "../../../context/DialogContext.tsx";
import CrossIcon from "../../../assets/icons/ic_cross.svg?react";
import {FriendshipStatus} from "../../../types/FriendshipStatus.ts";
import {AddFriendButton} from "../AddFriendButton";
import {AcceptButton} from "../AcceptButton";
import {DeclineButton} from "../DeclineButton";

interface ActionButtonsInterface {
    username: string;
    friendshipStatus?: FriendshipStatus;
    handleUpdateUserData: (username: string, newFriendshipStatus: FriendshipStatus) => void;
}

export const ActionButtons: FC<ActionButtonsInterface> = ({username, friendshipStatus, handleUpdateUserData}) => {
    const snackbar = useSnackBar();
    const dialog = useDialog();

    const handleDeleteFriend = (username: string) => {
        dialog.showDialog({
            title: "Delete friend",
            description: "Do you really want to delete a friend?",
            onSubmit: () => {
                apiClient.deleteFriend(username, {
                    onSuccess: () => {
                        handleUpdateUserData(username, undefined);
                        snackbar.showSnackBar(`Friend ${username} is deleted`, "success");
                    },
                    onFailure: e => {
                        snackbar.showSnackBar(`Can not delete friend ${username}`, "error");
                        console.error(e.message);
                    },
                });
            },
            submitTitle: "Delete",
            submitButtonIcon: <CrossIcon/>
        });
    }

    if (!friendshipStatus) {
        return (
            <AddFriendButton username={username} handleUpdateUserData={handleUpdateUserData}/>
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
                        <AcceptButton username={username} handleUpdateUserData={handleUpdateUserData}/>
                        <DeclineButton username={username} handleUpdateUserData={handleUpdateUserData}/>
                    </Box>
                )
            }
        </Box>
    )
}
