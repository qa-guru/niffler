import {SecondaryButton} from "../../Button";
import AddFriendIcon from "../../../assets/icons/ic_add_friend.svg?react";
import {Box, useMediaQuery, useTheme} from "@mui/material";
import {FC} from "react";
import {apiClient} from "../../../api/apiClient.ts";
import {useSnackBar} from "../../../context/SnackBarContext.tsx";
import {FriendshipStatus} from "../../../types/FriendshipStatus.ts";

interface AddFriendButtonInterface {
    username: string;
    handleUpdateUserData: (username: string, newFriendshipStatus: FriendshipStatus) => void;
}

export const AddFriendButton: FC<AddFriendButtonInterface> = ({username, handleUpdateUserData}) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const snackbar = useSnackBar();

    const handleAddUser = (username: string) => {
        apiClient.sendInvitation(username, {
            onSuccess: (data) => {
                handleUpdateUserData(data.username, data.friendshipStatus);
                snackbar.showSnackBar(`Invitation sent to ${username}`, "success");
            },
            onFailure: e => {
                snackbar.showSnackBar(`Failed to send invitation to ${username}`, "error");
                console.error(e.message);
            },
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