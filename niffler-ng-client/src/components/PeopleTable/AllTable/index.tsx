import {useEffect, useState} from "react";
import {PeopleTable} from ".."
import {TableToolbar} from "../../Table/TableToolbar";
import {TableContainer} from "@mui/material";
import {TablePagination} from "../../Table/Pagination";
import {apiClient} from "../../../api/apiClient.ts";
import {User} from "../../../types/User.ts";
import {EmptyUsersState} from "../../EmptyUsersState";

export const AllTable = () => {
    const [page, setPage] = useState(0);
    const [hasPreviousPage, setHasPreviousPage] = useState(false);
    const [hasLastPage, setHasLastPage] = useState(false);
    const [search, setSearch] = useState("");
    const [people, setPeople] = useState<User[]>([]);

    useEffect(() => {
        apiClient.getAllPeople(search, page, {
            onSuccess: data => {
                setPeople(data.content);
                setHasPreviousPage(!data.first);
                setHasLastPage(!data.last);
            },
            onFailure: e => console.log(e),
        })
    }, [search, page]);

    const handleInputSearch = (value: string) => {
        setSearch(value);
        setPage(0);
    }


    return (
        <TableContainer sx={{
            width: 700,
            margin: "0 auto",
        }}>
            <TableToolbar onSearchSubmit={handleInputSearch}/>
            {
                people.length > 0 ? (
                        <PeopleTable
                            data={people}
                            setData={setPeople}
                        />
                    ) :
                    (<EmptyUsersState/>)
            }
            <TablePagination
                onPreviousClick={() => setPage(page - 1)}
                onNextClick={() => setPage(page + 1)}
                hasPreviousValues={hasPreviousPage}
                hasNextValues={hasLastPage}
            />
        </TableContainer>

    )
}