import {SecondaryButton} from "../../Button";
import AddFriendIcon from "../../../assets/icons/ic_add_friend.svg?react";
import {Box, useMediaQuery, useTheme} from "@mui/material";
import {FC} from "react";
import {useSnackBar} from "../../../context/SnackBarContext.tsx";
import {FriendshipAction, useUpdateFriendshipStatusMutation} from "../../../generated/graphql.tsx";

interface AddFriendButtonInterface {
    username: string;
    onUpdateCallback: () => void;
}

export const AddFriendButton: FC<AddFriendButtonInterface> = ({username, onUpdateCallback}) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const snackbar = useSnackBar();
    const [updateFriendship] = useUpdateFriendshipStatusMutation();


    const handleAddUser = (username: string) => {
        updateFriendship({
            variables: {
                input: {
                    username: username,
                    action: FriendshipAction.Add,
                }
            },
            errorPolicy: "none",
            onCompleted: () => {
                onUpdateCallback();
                snackbar.showSnackBar(`Invitation sent to ${username}`, "success");
            },
            onError: (err) => {
                snackbar.showSnackBar(`Failed to send invitation to ${username}`, "error");
                console.error(err);
            }
        });
    }

    return (
        <Box sx={{
            margin: 1
        }}>
            {
                isMobile
                    ? <SecondaryButton
                        type="button"
                        variant="contained"
                        color="secondary"
                        size="small"
                        sx={{padding: 1, minWidth: "24px"}}
                        onClick={() => handleAddUser(username)}
                    >
                        <AddFriendIcon stroke={theme.palette.black.main}/>
                    </SecondaryButton>
                    : <SecondaryButton
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
            }
        </Box>
    )
}