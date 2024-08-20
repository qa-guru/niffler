import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import {FC, MouseEvent, useContext, useState} from 'react';
import {Link} from 'react-router-dom';
import logo from "../../assets/icons/coin.svg";
import "./styles.css";
import {SessionContext, USER_INITIAL_STATE} from "../../context/SessionContext.tsx";
import {HeaderMenu} from "./HeaderMenu";
import {NewSpendingButton} from "./NewSpendingButton";
import {MenuButton} from "./MenuButton";
import {useMediaQuery, useTheme} from "@mui/material";
import {MobileHeaderMenu} from "./MobileHeaderMenu";
import {useDialog} from "../../context/DialogContext.tsx";
import {authClient} from "../../api/authClient.ts";
import {clearSession, initLocalStorageAndRedirectToAuth} from "../../api/authUtils.ts";

export const MenuAppBar: FC = () => {
    const {user, updateUser} = useContext(SessionContext);
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);

    const handleClose = () => {
        setAnchorEl(null);
    };

    const onAvatarClick = (event: MouseEvent<HTMLElement>) => {
        open
            ? setAnchorEl(null)
            : setAnchorEl(event.currentTarget);
    };

    const dialog = useDialog();

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
        <>
            <AppBar color="secondary" sx={{
                position: (isMobile && open) ? "relative" : "fixed",
                zIndex: theme.zIndex.drawer + 1,
            }}>
                <Toolbar sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    marginLeft: isMobile ? 1 : 4,
                    marginRight: isMobile ? 1 : 4,

                }}>
                    {
                        isMobile
                            ?
                            <>
                                <MenuButton
                                    onMenuClick={onAvatarClick}
                                    isMenuOpened={open}
                                    photo={user?.photoSmall}
                                />
                                <Box>
                                    <Link to={"/main"} className="link">
                                        <img src={logo} alt="Niffler logo" width={30} height={30}/>
                                        <Typography variant="h5" component="h1" sx={{flexGrow: 1, marginLeft: 1}}>
                                            Niffler
                                        </Typography>
                                    </Link>
                                </Box>
                                <NewSpendingButton/>
                            </>
                            :
                            <>
                                <Box>
                                    <Link to={"/main"} className="link">
                                        <img src={logo} alt="Niffler logo" width={30} height={30}/>
                                        <Typography variant="h5" component="h1" sx={{flexGrow: 1, marginLeft: 1}}>
                                            Niffler
                                        </Typography>
                                    </Link>
                                </Box>
                                <Box>
                                    <NewSpendingButton/>
                                    <MenuButton onMenuClick={onAvatarClick}
                                                isMenuOpened={open}
                                                photo={user?.photoSmall}/>
                                </Box>
                            </>
                    }
                </Toolbar>
            </AppBar>
            {isMobile ?
                <MobileHeaderMenu handleClose={handleClose} open={open} handleLogout={handleLogout}/>
                :
                <HeaderMenu handleClose={handleClose} open={open} anchorElement={anchorEl} handleLogout={handleLogout}/>
            }
        </>
    );
}
