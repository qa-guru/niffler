import {Divider, ListItemIcon, Menu, MenuItem} from "@mui/material";
import {Link} from "react-router-dom";
import {Icon} from "../../Icon";
import {FC} from "react";

interface HeaderMenuInterface {
    open: boolean;
    handleClose: () => void;
    anchorElement: null | HTMLElement;
    handleLogout: () => void;
}

export const HeaderMenu: FC<HeaderMenuInterface> = ({handleClose, anchorElement, open, handleLogout}) => {

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
            <MenuItem sx={{width: "212px", padding: 0}} onClick={handleClose}>
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