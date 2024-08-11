import {FC} from "react";
import {useSnackBar} from "../../../context/SnackBarContext";
import {Box, Chip, useTheme} from "@mui/material";
import AddFriendIcon from "../../../assets/icons/ic_add_friend.svg?react";
import {PrimaryButton, SecondaryButton} from "../../Button";
import {Icon} from "../../Icon";
import {apiClient} from "../../../api/apiClient.ts";
import {useDialog} from "../../../context/DialogContext.tsx";
import CrossIcon from "../../../assets/icons/ic_cross.svg?react";
import {FriendState} from "../../../types/FriendState.ts";

interface ActionButtonsInterface {
    username: string;
    friendState?: FriendState;
    handleUpdateUserData: (username: string, newFriendState: "FRIEND" | "INVITE_SENT" | "INVITE_RECEIVED" | undefined) => void;
}

export const ActionButtons: FC<ActionButtonsInterface> = ({username, friendState, handleUpdateUserData}) => {
    const snackbar = useSnackBar();
    const dialog = useDialog();
    const theme = useTheme();

    const handleAddUser = (username: string) => {
        apiClient.sendInvitation(username, {
            onSuccess: (data) => {
                handleUpdateUserData(data.username, data.friendState);
                snackbar.showSnackBar(`Invitation sent to ${username}`, "success");
            },
            onFailure: e => {
                snackbar.showSnackBar(`Failed to send invitation to ${username}`, "error");
                console.error(e.message);
            },
        });
    }

    const handleAcceptInvitation = (username: string) => {
        apiClient.acceptInvitation(username, {
            onSuccess: (data) => {
                handleUpdateUserData(data.username, data.friendState);
                snackbar.showSnackBar(`Invitation of ${username} accepted`, "success");
            },
            onFailure: e => {
                snackbar.showSnackBar(`Can not accept invitation of ${username}`, "error");
                console.error(e.message);
            },
        });
    }

    const handleDeclineInvitation = (username: string) => {
        dialog.showDialog({
            title: "Decline friendship",
            description: "Do you really want to decline friendship?",
            onSubmit: () => {
                apiClient.declineInvitation(username, {
                    onSuccess: (data) => {
                        handleUpdateUserData(data.username, data.friendState);
                        snackbar.showSnackBar(`Invitation of ${username} is declined`, "success");
                    },
                    onFailure: e => {
                        snackbar.showSnackBar(`Can not decline invitation of ${username}`, "error");
                        console.error(e.message);
                    },
                });
            },
            submitTitle: "Decline",
            submitButtonIcon: <CrossIcon/>
        });
    }

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

    if (!friendState) {
        return (
            <Box sx={{
                margin: 1
            }}>
                <SecondaryButton
                    startIcon={
                        <AddFriendIcon stroke={theme.palette.black.main}/>}
                    type="button"
                    variant="contained"
                    color="secondary"
                    size="small"
                    onClick={() => handleAddUser(username)}
                >
                    Add friend
                </SecondaryButton>
            </Box>
        )
    }

    return (
        <Box sx={{
            margin: 1
        }}>
            {
                friendState === "FRIEND" && (
                    <SecondaryButton
                        type="button"
                        size="small"
                        onClick={() => handleDeleteFriend(username)}
                    >
                        Unfriend
                    </SecondaryButton>
                )}
            {
                friendState === "INVITE_SENT" && (
                    <Chip
                        sx={{
                            width: 100
                        }}
                        label="Waiting..."
                    />

                )
            }
            {
                friendState === "INVITE_RECEIVED" && (
                    <>
                        <PrimaryButton
                            startIcon={
                                <Icon type={"checkIcon"}/>}
                            type="button"
                            variant="contained"
                            size="small"
                            onClick={() => handleAcceptInvitation(username)}
                        >
                            Accept
                        </PrimaryButton>
                        <SecondaryButton
                            startIcon={
                                <Icon type={"crossIcon"}/>
                            }
                            type="button"
                            size="small"
                            sx={{
                                marginLeft: 2,
                            }}
                            onClick={() => handleDeclineInvitation(username)}
                        >
                            Decline
                        </SecondaryButton>
                    </>
                )
            }
        </Box>
    )
}
