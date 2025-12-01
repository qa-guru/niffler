import {Box} from "@mui/material";
import {FC} from "react";

interface TabPanelProps {
    children?: React.ReactNode;
    value: string;
}

export const TabPanel: FC<TabPanelProps> = ({children, value}) => {

    return (
        <div
            role="tabpanel"
            id={`simple-tabpanel-${value}`}
            aria-labelledby={`simple-tab-${value}`}
        >
            <Box sx={{p: 3}}>
                {children}
            </Box>
        </div>
    );
}
