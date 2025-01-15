import {Box, Divider, Drawer, List, ListItem, ListItemIcon, Typography} from "@mui/material";
import {FC} from "react";
import {Link} from "react-router-dom";
import {Icon} from "../../Icon";

interface MobileHeaderMenuInterface {
    open: boolean;
    handleClose: () => void;
    handleLogout: () => void;
}

export const MobileHeaderMenu: FC<MobileHeaderMenuInterface> = ({open, handleClose, handleLogout}) => {

    return (
        <Drawer open={open} onClose={handleClose} sx={{width: "100%"}}>
            <Box sx={{
                width: "100%",
                marginTop: "56px",
            }} role="presentation" onClick={() => handleClose}>
                <Typography
                    sx={{
                        margin: 2,
                    }}
                    variant="h5"
                    component="h2"
                >
                    Menu
                </Typography>
                <List>
                    <ListItem key={"profile"} onClick={handleClose}>
                        <Link to={"/profile"} className={"link nav-link"}>
                            <ListItemIcon sx={{padding: "8px 12px"}}>
                                <Icon type="userIcon"/>
                            </ListItemIcon>
                            Profile
                        </Link>
                    </ListItem>
                    <Divider/>
                    <ListItem key={"friends"} onClick={handleClose}>
                        <Link to={"/people/friends"} className={"link nav-link"}>
                            <ListItemIcon sx={{padding: "8px 12px"}}>
                                <Icon type="friendsIcon"/>
                            </ListItemIcon>
                            Friends
                        </Link>
                    </ListItem>
                    <ListItem key={"allPeople"} onClick={handleClose}>
                        <Link to={"/people/all"} className={"link nav-link"}>
                            <ListItemIcon sx={{padding: "8px 12px"}}>
                                <Icon type="allIcon"/>
                            </ListItemIcon>
                            All People
                        </Link>
                    </ListItem>
                    <Divider/>
                    <ListItem onClick={handleLogout}>
                        <ListItemIcon sx={{padding: "8px 12px"}}>
                            <Icon type="signOutIcon"/>
                        </ListItemIcon>
                        Sign out
                    </ListItem>
                </List>
            </Box>
        </Drawer>

    );
}