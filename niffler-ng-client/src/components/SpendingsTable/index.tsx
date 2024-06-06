import {Box, IconButton, InputBase, TableContainer, Toolbar, useTheme} from "@mui/material";
import {TablePagination} from "../Table/Pagination";
import {useState} from "react";
import SearchIcon from "@mui/icons-material/Search";
import {SecondaryButton} from "../Button";

export const SpendingsTable = () => {
    const [page, setPage] = useState(0);
    const [search, setSearch] = useState("");
    const theme = useTheme();

    const hasPreviousPage = false;
    const hasNextPage = true;

    return (
        <TableContainer sx={{
            width: 700,
            margin: "0 auto",
        }}>
            <Toolbar>
                <Box
                    component="form"
                    sx={{ display: 'flex', alignItems: 'center', width: "100%", backgroundColor: theme.palette.secondary.light, border: "1px solid #E4E6F1", borderRadius: "8px" }}
                    onSubmit={() => {}}
                >
                    <InputBase
                        sx={{ ml: 1, flex: 1}}
                        placeholder="Search"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                        inputProps={{ 'aria-label': 'search people' }}
                    />
                    <IconButton type="submit" sx={{ p: '10px' }} aria-label="search" color={"primary"}>
                        <SearchIcon />
                    </IconButton>
                </Box>
                <SecondaryButton>
                    Delete
                </SecondaryButton>

            </Toolbar>

            <TablePagination
                onPreviousClick={() => setPage(page - 1)}
                onNextClick={() => setPage(page + 1)}
                hasPreviousValues={hasPreviousPage}
                hasNextValues={hasNextPage}
            />
        </TableContainer>
    )
}