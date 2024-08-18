import {Box, Typography} from "@mui/material";
import niffler from "../../assets/images/niffler-with-a-coin.png";
import {FC} from "react";

interface EmptyTableStateInterface {
    title: string;
}

export const EmptyTableState: FC<EmptyTableStateInterface> = ({title}) => {
    return (
        <Box sx={{
            textAlign: "center",
            width: "100%",
            paddingTop: 15,
            paddingBottom: 10,
        }}>
            <Typography variant="h6" component="p" sx={{fontWeight: 700}}>{title}</Typography>
            <Box
                component="img"
                sx={{
                    height: 100,
                    width: 100,
                }}
                alt="Lonely niffler"
                src={niffler}
            />
        </Box>
    )
}
