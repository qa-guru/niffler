import {FriendshipStatus} from "../../../types/FriendshipStatus.ts";
import {FC} from "react";
import {useMediaQuery, useTheme} from "@mui/material";
import {useSnackBar} from "../../../context/SnackBarContext.tsx";
import {apiClient} from "../../../api/apiClient.ts";
import {PrimaryButton} from "../../Button";
import CheckIcon from "../../../assets/icons/ic_check.svg?react";

interface AcceptButtonInterface {
    username: string;
    handleUpdateUserData: (username: string, newFriendshipStatus: FriendshipStatus) => void;
}

export const AcceptButton: FC<AcceptButtonInterface> = ({username, handleUpdateUserData}) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const snackbar = useSnackBar();

    const handleAcceptInvitation = (username: string) => {
        apiClient.acceptInvitation(username, {
            onSuccess: (data) => {
                handleUpdateUserData(data.username, data.friendshipStatus);
                snackbar.showSnackBar(`Invitation of ${username} accepted`, "success");
            },
            onFailure: e => {
                snackbar.showSnackBar(`Can not accept invitation of ${username}`, "error");
                console.error(e.message);
            },
        });
    }

    return (
        <>
            {
                isMobile
                    ? <PrimaryButton
                        type="button"
                        variant="contained"
                        size="small"
                        sx={{padding: 1, minWidth: "24px"}}
                        onClick={() => handleAcceptInvitation(username)}
                    >
                        <CheckIcon/>
                    </PrimaryButton>
                    : <PrimaryButton
                        startIcon={<CheckIcon/>}
                        type="button"
                        variant="contained"
                        size="small"
                        onClick={() => handleAcceptInvitation(username)}
                    >
                        Accept
                    </PrimaryButton>
            }
        </>
    )
}