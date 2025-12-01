import {FriendshipStatus} from "../../../types/FriendshipStatus.ts";
import {FC} from "react";
import {useMediaQuery, useTheme} from "@mui/material";
import {useSnackBar} from "../../../context/SnackBarContext.tsx";
import {apiClient} from "../../../api/apiClient.ts";
import {SecondaryButton} from "../../Button";
import CrossIcon from "../../../assets/icons/ic_cross.svg?react";
import {useDialog} from "../../../context/DialogContext.tsx";

interface DeclineButtonInterface {
    username: string;
    handleUpdateUserData: (username: string, newFriendshipStatus: FriendshipStatus) => void;
}

export const DeclineButton: FC<DeclineButtonInterface> = ({username, handleUpdateUserData}) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const snackbar = useSnackBar();
    const dialog = useDialog();

    const handleDeclineInvitation = (username: string) => {
        dialog.showDialog({
            title: "Decline friendship",
            description: "Do you really want to decline friendship?",
            onSubmit: () => {
                apiClient.declineInvitation(username, {
                    onSuccess: (data) => {
                        handleUpdateUserData(data.username, data.friendshipStatus);
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

    return (
        <>
            {
                isMobile
                    ? <SecondaryButton
                        type="button"
                        size="small"
                        sx={{
                            marginLeft: 2,
                            padding: 1,
                            minWidth: "24px"
                        }}
                        onClick={() => handleDeclineInvitation(username)}
                    >
                        <CrossIcon/>
                    </SecondaryButton>
                    : <SecondaryButton
                        startIcon={
                            <CrossIcon/>
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
            }
        </>
    )
}