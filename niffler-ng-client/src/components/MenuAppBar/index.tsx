import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import {FC, MouseEvent, useContext, useState} from 'react';
import {Link} from 'react-router-dom';
import logo from "../../assets/icons/coin.svg";
import "./styles.css";
import {SessionContext} from "../../context/SessionContext.tsx";
import {HeaderMenu} from "./HeaderMenu";
import {NewSpendingButton} from "./NewSpendingButton";
import {MenuButton} from "./MenuButton";
import {useMediaQuery, useTheme} from "@mui/material";

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
        setAnchorEl(event.currentTarget);
    }

    return (
        <>
            <AppBar position="fixed" color="secondary">
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
                                <MenuButton onMenuClick={onAvatarClick} isMenuOpened={open} photo={user?.photoSmall}/>
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
                                    <MenuButton onMenuClick={onAvatarClick} isMenuOpened={open}
                                                photo={user?.photoSmall}/>
                                </Box>
                            </>
                    }
                </Toolbar>
            </AppBar>
            <HeaderMenu handleClose={handleClose} open={open} anchorElement={anchorEl} updateUser={updateUser}/>
        </>
    );
}
