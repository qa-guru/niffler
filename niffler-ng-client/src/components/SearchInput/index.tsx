import {Box, IconButton, InputBase, useTheme} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import CrossIcon from "../../assets/icons/ic_cross.svg?react";
import {FC, FormEvent, useState} from "react";

interface SearchInputInterface {
    onSearchSubmit: (value: string) => void;
}

export const SearchInput: FC<SearchInputInterface> = ({onSearchSubmit}) => {
    const theme = useTheme();
    const [search, setSearch] = useState("");

    const handleSubmitSearch = (e: FormEvent) => {
        e.preventDefault();
        onSearchSubmit(search);
    }

    return (
        <Box sx={{
            display: 'flex',
            alignItems: 'center',
            width: "100%",
            backgroundColor: theme.palette.secondary.light,
            border: "1px solid #E4E6F1",
            borderRadius: "8px",
            padding: "0.1rem",
        }}
             component="form"
             onSubmit={handleSubmitSearch}
        >
            <InputBase
                sx={{ml: 1, flex: 1}}
                placeholder="Search"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                inputProps={{'aria-label': 'search'}}
            />
            {
                search?.length > 0
                    ? <IconButton
                        type="button"
                        id="input-clear"
                        sx={{p: '10px'}}
                        aria-label="search"
                        color={"primary"}
                        onClick={() => setSearch("")}
                    >
                        <CrossIcon/>
                    </IconButton>
                    : <IconButton
                        type="submit"
                        id="input-submit"
                        sx={{p: '10px'}}
                        aria-label="search"
                        color={"primary"}
                    >
                        <SearchIcon/>
                    </IconButton>
            }
        </Box>
    )
}