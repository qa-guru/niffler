import {Box, IconButton, InputBase, useTheme} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import CrossIcon from "../../assets/icons/ic_cross.svg?react";
import {FC, useEffect, useState} from "react";
import {useDebounce} from "../../hooks/useDebounce.ts";

interface SearchInputInterface {
    onSearchSubmit: (value: string) => void;
    value?: string;
    onChange?: (value: string) => void;
}

export const SearchInput: FC<SearchInputInterface> = ({onSearchSubmit, value, onChange}) => {
    const theme = useTheme();
    const [innerValue, setInnerValue] = useState("");
    const search = value ?? innerValue;
    const setSearch = onChange ?? setInnerValue;
    const debouncedSearch = useDebounce(search, 300);

    useEffect(() => {
        onSearchSubmit(debouncedSearch);
    }, [debouncedSearch]);

    const handleClear = () => {
        setSearch("");
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
        }}>
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
                        aria-label="clear"
                        color={"primary"}
                        onClick={handleClear}
                    >
                        <CrossIcon/>
                    </IconButton>
                    : <IconButton
                        type="button"
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
