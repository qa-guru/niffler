import {FC} from "react";
import {useMediaQuery, useTheme} from "@mui/material";
import {useSnackBar} from "../../../context/SnackBarContext.tsx";
import {SecondaryButton} from "../../Button";
import CrossIcon from "../../../assets/icons/ic_cross.svg?react";
import {useDialog} from "../../../context/DialogContext.tsx";
import {FriendshipAction, useUpdateFriendshipStatusMutation} from "../../../generated/graphql.tsx";

interface DeclineButtonInterface {
    username: string;
    onUpdateCallback: () => void;
}

export const DeclineButton: FC<DeclineButtonInterface> = ({username, onUpdateCallback}) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const snackbar = useSnackBar();
    const dialog = useDialog();
    const [updateFriendship] = useUpdateFriendshipStatusMutation();

    const handleDeclineInvitation = (username: string) => {
        dialog.showDialog({
            title: "Decline friendship",
            description: "Do you really want to decline friendship?",
            onSubmit: () => {
                updateFriendship({
                    variables: {
                        input: {
                            username: username,
                            action: FriendshipAction.Reject,
                        }
                    },
                    errorPolicy: "none",
                    onCompleted: () => {
                        onUpdateCallback();
                        snackbar.showSnackBar(`Invitation of ${username} is declined`, "success");
                    },
                    onError: (err) => {
                        snackbar.showSnackBar(`Can not decline invitation of ${username}`, "error");
                        console.error(err);
                    }
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