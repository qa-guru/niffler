import {FC} from "react";
import {useMediaQuery, useTheme} from "@mui/material";
import {useSnackBar} from "../../../context/SnackBarContext.tsx";
import {PrimaryButton} from "../../Button";
import CheckIcon from "../../../assets/icons/ic_check.svg?react";
import {FriendshipAction, useUpdateFriendshipStatusMutation} from "../../../generated/graphql.tsx";

interface AcceptButtonInterface {
    username: string;
    onUpdateCallback: () => void;
}

export const AcceptButton: FC<AcceptButtonInterface> = ({username, onUpdateCallback}) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const snackbar = useSnackBar();
    const [updateFriendship] = useUpdateFriendshipStatusMutation();

    const handleAcceptInvitation = (username: string) => {
        updateFriendship({
            variables: {
                input: {
                    username: username,
                    action: FriendshipAction.Accept,
                }
            },
            errorPolicy: "none",
            onCompleted: () => {
                onUpdateCallback();
                snackbar.showSnackBar(`Invitation of ${username} accepted`, "success");
            },
            onError: (err) => {
                snackbar.showSnackBar(`Can not accept invitation of ${username}`, "error");
                console.error(err);
            }
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