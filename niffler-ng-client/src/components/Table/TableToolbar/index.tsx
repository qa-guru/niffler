import {Box, IconButton, InputBase, Toolbar, useTheme} from "@mui/material";
import {FC, FormEvent, useState} from "react";
import SearchIcon from '@mui/icons-material/Search';


interface TableToolbarProps {
    onSearchSubmit: (value: string) => void;
}

export const TableToolbar: FC<TableToolbarProps> = ({ onSearchSubmit}) => {
    const [value, setValue]  = useState("")
    const theme = useTheme();

    const handleSubmitSearch = (e: FormEvent) => {
        e.preventDefault();
        onSearchSubmit(value);
    }

    return (
        <Toolbar>
            <Box
                component="form"
                sx={{ display: 'flex', alignItems: 'center', width: "100%", backgroundColor: theme.palette.secondary.light, border: "1px solid #E4E6F1", borderRadius: "8px", marginBottom: 4 }}
                onSubmit={handleSubmitSearch}
            >
                <InputBase
                    sx={{ ml: 1, flex: 1}}
                    placeholder="Search"
                    value={value}
                    onChange={(e) => setValue(e.target.value)}
                    inputProps={{ 'aria-label': 'search people' }}
                />
                <IconButton type="submit" sx={{ p: '10px' }} aria-label="search" color={"primary"}>
                    <SearchIcon />
                </IconButton>
            </Box>
        </Toolbar>
    );
}