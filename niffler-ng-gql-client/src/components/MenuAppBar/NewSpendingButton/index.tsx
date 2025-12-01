import {Button, useMediaQuery, useTheme} from "@mui/material";
import {Link} from "react-router-dom";
import PlusIcon from "../../../assets/icons/ic_plus.svg?react"

export const NewSpendingButton = () => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    return (
        isMobile
            ?
            <Button
                color={"primary"}
                variant={"contained"}
                component={Link}
                to={"/spending"}
                sx={{padding: 1, minWidth: "24px"}}
            >
                <PlusIcon/>
            </Button>
            : <Button
                color={"primary"}
                variant={"contained"}
                startIcon={<PlusIcon color={theme.palette.gray_600.main}/>}
                component={Link}
                to={"/spending"}
            >
                New spending
            </Button>
    );
}