import {Divider, ListItemIcon, Menu, MenuItem, Typography, useMediaQuery, useTheme} from "@mui/material";
import {Link} from "react-router-dom";
import {Icon} from "../../Icon";
import {authClient} from "../../../api/authClient.ts";
import {clearSession, initLocalStorageAndRedirectToAuth} from "../../../api/authUtils.ts";
import {USER_INITIAL_STATE} from "../../../context/SessionContext.tsx";
import {useDialog} from "../../../context/DialogContext.tsx";
import {FC} from "react";
import {User} from "../../../types/User.ts";

interface HeaderMenuInterface {
    open: boolean;
    handleClose: () => void;
    anchorElement: null | HTMLElement;
    updateUser: (user: User) => void;

}

export const HeaderMenu: FC<HeaderMenuInterface> = ({handleClose, anchorElement, open, updateUser}) => {
    const dialog = useDialog();
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    const handleLogout = () => {
        handleClose();
        dialog.showDialog({
            title: "Want to logout?",
            description: "If you are sure, submit your action.",
            onSubmit: () => {
                authClient.logout({
                    onSuccess: () => {
                        clearSession();
                        updateUser(USER_INITIAL_STATE);
                        initLocalStorageAndRedirectToAuth();
                    },
                    onFailure: () => {
                    },
                });
            },
            submitTitle: "Log out",
        });
    }

    return (
        <Menu
            anchorEl={anchorElement}
            id="account-menu"
            open={open}
            onClose={handleClose}
            transformOrigin={{horizontal: 'right', vertical: 'top'}}
            anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
            sx={{
                margin: 0,
            }}
        >
            {isMobile &&
                <Typography
                    variant="h5"
                    component="h2"
                >
                    Menu
                </Typography>
            }
            <MenuItem sx={{width: isMobile ? "100vw" : "212px", padding: 0}} onClick={handleClose}>
                <Link to={"/profile"} className={"link nav-link"}>
                    <ListItemIcon sx={{padding: "8px 12px"}}>
                        <Icon type="userIcon"/>
                    </ListItemIcon>
                    Profile
                </Link>
            </MenuItem>
            <Divider/>
            <MenuItem onClick={handleClose} sx={{padding: 0}}>
                <Link to={"/people/friends"} className={"link nav-link"}>
                    <ListItemIcon sx={{padding: "8px 12px"}}>
                        <Icon type="friendsIcon"/>
                    </ListItemIcon>
                    Friends
                </Link>
            </MenuItem>
            <MenuItem onClick={handleClose} sx={{padding: 0}}>
                <Link to={"/people/all"} className={"link nav-link"}>
                    <ListItemIcon sx={{padding: "8px 12px"}}>
                        <Icon type="allIcon"/>
                    </ListItemIcon>
                    All People
                </Link>
            </MenuItem>
            <Divider/>
            <MenuItem onClick={handleLogout} sx={{padding: 0}}>
                <ListItemIcon sx={{padding: "8px 12px"}}>
                    <Icon type="signOutIcon"/>
                </ListItemIcon>
                Sign out
            </MenuItem>
        </Menu>
    )
}