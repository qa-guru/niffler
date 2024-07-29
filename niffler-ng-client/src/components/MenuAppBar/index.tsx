import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import {FC, useState, MouseEvent, useContext} from 'react';
import {Link} from 'react-router-dom';
import logo from "../../assets/icons/coin.svg";
import {Icon} from "../Icon";
import {Avatar, Button, Divider, ListItemIcon, Menu, MenuItem} from "@mui/material";
import "./styles.css";
import {SessionContext, USER_INITIAL_STATE} from "../../context/SessionContext.tsx";
import {authClient} from "../../api/authClient.ts";
import {clearSession, initLocalStorageAndRedirectToAuth} from "../../api/authUtils.ts";
import {useDialog} from "../../context/DialogContext.tsx";

export const MenuAppBar: FC = () => {
    const {user, updateUser} = useContext(SessionContext);
    const dialog = useDialog();

    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);

    const handleClose = () => {
        setAnchorEl(null);
    };

    const onAvatarClick = (event: MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    }

    const handleLogout = () => {
        handleClose();
        dialog.showDialog({
            title: "Want to logout?",
            description: "If you are sure, submit your action",
            onSubmit: () => {
                authClient.logout({
                    onSuccess: () => {
                        clearSession();
                        updateUser(USER_INITIAL_STATE);
                        initLocalStorageAndRedirectToAuth();
                    },
                    onFailure: () => {},
                });
            },
            submitTitle: "Log out"
        });

    }

    return (
        <>
            <AppBar position="fixed" color="secondary">
                <Toolbar sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    marginLeft: 4,
                    marginRight: 4,
                }}>
                    <Link to={"/main"} className="link">
                        <img src={logo} alt="Niffler logo" width={26} height={26}/>
                        <Typography variant="h5" component="h1" sx={{flexGrow: 1, marginLeft: 1}}>
                            Niffler
                        </Typography>
                    </Link>
                    <Box>
                        <Button
                            color={"primary"}
                            variant={"contained"}
                            startIcon={<Icon type="plusIcon"/>}
                            component={Link}
                            to={"/spending"}
                        >
                            New spending
                        </Button>
                        <IconButton
                            size="large"
                            aria-label="Menu"
                            onClick={onAvatarClick}
                            color="inherit"
                            aria-controls={open ? 'account-menu' : undefined}
                            aria-haspopup="true"
                            aria-expanded={open ? 'true' : undefined}
                        >
                            <Avatar sx={{ width: 48, height: 48 }} src={user.photoSmall}/>
                        </IconButton>
                    </Box>
                </Toolbar>
            </AppBar>
            <Menu
                anchorEl={anchorEl}
                id="account-menu"
                open={open}
                onClose={handleClose}
                transformOrigin={{ horizontal: 'right', vertical: 'top' }}
                anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
            >
                <MenuItem sx={{
                    width: "212px",
                }}>
                    <Link to={"/profile"} className={"link nav-link"}>
                        <ListItemIcon>
                            <Icon type="userIcon"/>
                        </ListItemIcon>
                        Profile
                    </Link>
                </MenuItem>
                <Divider />
                <MenuItem onClick={() => {}}>
                    <Link to={"/people/friends"} className={"link nav-link"}>
                        <ListItemIcon>
                            <Icon type="friendsIcon"/>
                        </ListItemIcon>
                        Friends
                    </Link>
                </MenuItem>
                <MenuItem onClick={() => {}}>
                    <Link to={"/people/all"} className={"link nav-link"}>
                        <ListItemIcon>
                            <Icon type="allIcon"/>
                        </ListItemIcon>
                        All People
                    </Link>
                </MenuItem>
                <Divider />
                <MenuItem onClick={handleLogout}>
                    <ListItemIcon>
                        <Icon type="signOutIcon"/>
                    </ListItemIcon>
                    Sign out
                </MenuItem>
            </Menu>
        </>
    );
}