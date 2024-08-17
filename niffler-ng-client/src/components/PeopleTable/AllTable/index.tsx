import {useEffect, useState} from "react";
import {PeopleTable} from ".."
import {TableToolbar} from "../../Table/TableToolbar";
import {Box, TableContainer} from "@mui/material";
import {TablePagination} from "../../Table/Pagination";
import {apiClient} from "../../../api/apiClient.ts";
import {User} from "../../../types/User.ts";
import {EmptyTableState} from "../../EmptyUsersState";
import {Loader} from "../../Loader";
import {usePrevious} from "../../../hooks/usePrevious.ts";
import {useSnackBar} from "../../../context/SnackBarContext.tsx";

export const AllTable = () => {
    const [page, setPage] = useState(0);
    const prevPage = usePrevious(page);
    const [hasPreviousPage, setHasPreviousPage] = useState(false);
    const [hasLastPage, setHasLastPage] = useState(false);
    const [search, setSearch] = useState("");
    const [people, setPeople] = useState<User[]>([]);
    const [isButtonLoading, setIsButtonLoading] = useState<boolean>(false);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const snackbar = useSnackBar();

    useEffect(() => {
        ((!prevPage && page === 0) || page === prevPage) ? setIsLoading(true) : setIsButtonLoading(true);
        apiClient.getAllPeople(search, page, {
            onSuccess: data => {
                setPeople(data.content);
                setHasPreviousPage(!data.first);
                setHasLastPage(!data.last);
                setIsLoading(false);
                setIsButtonLoading(false);
            },
            onFailure: e => {
                console.error(e.message);
                snackbar.showSnackBar(e.message, "error");
                setIsLoading(false);
                setIsButtonLoading(false);
            },
        })
    }, [search, page]);

    const handleInputSearch = (value: string) => {
        setSearch(value);
        setPage(0);
    };


    return (
        <TableContainer sx={{
            maxWidth: 700,
            margin: "0 auto",
            position: "relative",
            minHeight: 200,
        }}>
            <Box sx={{marginBottom: 2, marginTop: 1}}>
                <TableToolbar onSearchSubmit={handleInputSearch}/>
            </Box>
            {isLoading
                ? <Loader/>
                : people.length > 0 ?
                    <>
                        <PeopleTable
                            data={people}
                            setData={setPeople}
                            label="all"
                        />
                        <TablePagination
                            onPreviousClick={() => setPage(page - 1)}
                            onNextClick={() => setPage(page + 1)}
                            hasPreviousValues={hasPreviousPage}
                            hasNextValues={hasLastPage}
                            isNextButtonLoading={isButtonLoading}
                            isPreviousButtonLoading={isButtonLoading}
                        />
                    </>
                    :
                    <EmptyTableState title={"There are no users yet"}/>
            }
        </TableContainer>

    )
}
