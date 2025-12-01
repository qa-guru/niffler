import IconButton from "@mui/material/IconButton";
import {Avatar, useMediaQuery, useTheme} from "@mui/material";
import {FC, MouseEvent} from "react";
import MenuIcon from "../../../assets/icons/ic_menu.svg?react";
import CrossIcon from "../../../assets/icons/ic_cross.svg?react";


interface MenuButtonInterface {
    onMenuClick: (event: MouseEvent<HTMLElement>) => void,
    isMenuOpened: boolean,
    photo?: string | null,
}

export const MenuButton: FC<MenuButtonInterface> = ({onMenuClick, isMenuOpened, photo}) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    return (
        <IconButton
            size="large"
            aria-label="Menu"
            onClick={onMenuClick}
            color="inherit"
            aria-controls={isMenuOpened ? 'account-menu' : undefined}
            aria-haspopup="true"
            aria-expanded={isMenuOpened ? 'true' : undefined}
            sx={{
                borderRadius: isMobile ? 1 : 8
            }}
        >
            {isMobile
                ? isMenuOpened ?
                    <CrossIcon color={theme.palette.blue100.main}/>
                    : <MenuIcon/>
                : <Avatar sx={{width: 48, height: 48}} src={photo || ""}/>
            }
        </IconButton>
    )
}