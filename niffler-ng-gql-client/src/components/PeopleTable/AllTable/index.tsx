import {useCallback, useEffect, useState} from "react";
import {PeopleTable} from ".."
import {TableToolbar} from "../../Table/TableToolbar";
import {Box, TableContainer} from "@mui/material";
import {TablePagination} from "../../Table/Pagination";
import {EmptyTableState} from "../../EmptyUsersState";
import {Loader} from "../../Loader";
import {useSnackBar} from "../../../context/SnackBarContext.tsx";
import {useAllPeopleQuery} from "../../../generated/graphql.tsx";

export const AllTable = () => {
    const [page, setPage] = useState(0);
    const [search, setSearch] = useState("");
    const snackbar = useSnackBar();
    const {data, loading, refetch} = useAllPeopleQuery({
        variables: {
            page,
            size: 10,
            searchQuery: search,
        },
        fetchPolicy: "cache-and-network",
        errorPolicy: "none",
        onError: (err) => {
            console.error(err);
            snackbar.showSnackBar("Error while loading all people data", "error");
        }
    });

    const loadPeople = useCallback(() => {
        refetch({
            page,
            size: 10,
            searchQuery: search,
        });
    }, [refetch, search, page]);

    useEffect(() => {
        loadPeople();
    }, [loadPeople]);


    const handleInputSearch = (value: string) => {
        setSearch(value);
        setPage(0);
    };

    const people = data?.allPeople?.edges
        .filter(edge => edge !== null)
        .map(edge => edge.node)


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
            {loading
                ? <Loader/>
                : data?.allPeople?.edges.length ?
                    <>
                        <PeopleTable
                            data={people ?? []}
                            onUpdateCallback={loadPeople}
                            label="all"
                        />
                        <TablePagination
                            onPreviousClick={() => setPage(page - 1)}
                            onNextClick={() => setPage(page + 1)}
                            hasPreviousValues={data.allPeople.pageInfo.hasPreviousPage}
                            hasNextValues={data?.allPeople.pageInfo.hasNextPage}
                            isNextButtonLoading={loading}
                            isPreviousButtonLoading={loading}
                        />
                    </>
                    :
                    <EmptyTableState title={"There are no users yet"}/>
            }
        </TableContainer>

    )
}
