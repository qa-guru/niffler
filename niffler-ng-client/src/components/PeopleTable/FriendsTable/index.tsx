import {useEffect, useState} from "react";
import { PeopleTable } from "..";
import {TableToolbar} from "../../Table/TableToolbar";
import {Box, TableContainer, Typography} from "@mui/material";
import {User} from "../../../types/User.ts";
import {TablePagination} from "../../Table/Pagination";
import {apiClient} from "../../../api/apiClient.ts";
import {EmptyUsersState} from "../../EmptyUsersState";

export const FriendsTable = () => {
    const [page, setPage] = useState(0);
    const [hasPreviousPage, setHasPreviousPage] = useState(false);
    const [hasLastPage, setHasLastPage] = useState(false);
    const [search, setSearch] = useState("");
    const [friends, setFriends] = useState<User[]>([]);
    const [invitations, setInvitations] = useState<User[]>([]);

    useEffect(() => {
        apiClient.getFriends(search, page, {
            onSuccess: data => {
                setFriends(data.content.filter(user => user.friendState === "FRIEND"));
                setInvitations(data.content.filter(user => user.friendState === "INVITE_RECEIVED"));
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
            { (friends.length === 0 && invitations.length === 0) &&
                <EmptyUsersState/>
            }

            {
                invitations.length > 0 &&
                <>
                    <Typography variant={"h5"} component={"h2"} sx={{ marginBottom: 2}}>Friend requests</Typography>
                    <PeopleTable
                        data={invitations}
                        setData={setInvitations}

                    />
                </>
            }
            {
                friends.length > 0 &&
                <>
                    <Typography variant={"h5"} component={"h2"} sx={{marginTop: 2, marginBottom: 2}}>My friends</Typography>
                    <PeopleTable
                        data={friends}
                        setData={setFriends}
                    />
                </>
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