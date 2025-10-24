import { Button, useMediaQuery, useTheme } from "@mui/material";
import { Link } from "react-router-dom";
import { AutoAwesome } from "@mui/icons-material";

export const AISpendingButton = () => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    return (
        isMobile
            ?
            <Button
                variant={"contained"}
                component={Link}
                to={"/ai-spending"}
                sx={{ 
                    padding: 1, 
                    minWidth: "24px", 
                    marginRight: 1,
                    backgroundColor: '#9c27b0',
                    '&:hover': {
                        backgroundColor: '#7b1fa2',
                    }
                }}
            >
                <AutoAwesome fontSize="small" />
            </Button>
            : <Button
                variant={"contained"}
                startIcon={<AutoAwesome />}
                component={Link}
                to={"/ai-spending"}
                sx={{ 
                    marginRight: 2,
                    backgroundColor: '#9c27b0',
                    color: '#fff',
                    '&:hover': {
                        backgroundColor: '#7b1fa2',
                    }
                }}
            >
                AI Spending
            </Button>
    );
}

