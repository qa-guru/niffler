import {useEffect, useState} from "react";
import {PeopleTable} from "..";
import {TableToolbar} from "../../Table/TableToolbar";
import {Box, TableContainer, Typography} from "@mui/material";
import {User} from "../../../types/User.ts";
import {TablePagination} from "../../Table/Pagination";
import {apiClient} from "../../../api/apiClient.ts";
import {EmptyTableState} from "../../EmptyUsersState";
import {Loader} from "../../Loader";
import {usePrevious} from "../../../hooks/usePrevious.ts";
import {useSnackBar} from "../../../context/SnackBarContext.tsx";

export const FriendsTable = () => {
    const [page, setPage] = useState(0);
    const prevPage = usePrevious(page);
    const [hasPreviousPage, setHasPreviousPage] = useState(false);
    const [hasLastPage, setHasLastPage] = useState(false);
    const [search, setSearch] = useState("");
    const [friends, setFriends] = useState<User[]>([]);
    const [invitations, setInvitations] = useState<User[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [isButtonLoading, setIsButtonLoading] = useState<boolean>(false);
    const snackbar = useSnackBar();

    useEffect(() => {
        ((!prevPage && page === 0) || page === prevPage) ? setIsLoading(true) : setIsButtonLoading(true);
        apiClient.getFriends(search, page, {
            onSuccess: data => {
                setFriends(data.content.filter(user => user.friendshipStatus === "FRIEND"));
                setInvitations(data.content.filter(user => user.friendshipStatus === "INVITE_RECEIVED"));
                setHasPreviousPage(!data.first);
                setHasLastPage(!data.last);
                setIsLoading(false);
                setIsButtonLoading(false);
            },
            onFailure: (e) => {
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
    }

    const handleUpdateInvitations = (data: User[]) => {
        setFriends([...friends, ...data.filter(user => user.friendshipStatus === "FRIEND")]);
        setInvitations(data.filter(user => user.friendshipStatus === "INVITE_RECEIVED"));
    }

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
                ? <Loader/> :
                (friends.length === 0 && invitations.length === 0)
                    ? <EmptyTableState title={"There are no users yet"}/>
                    : (
                        <>
                            {
                                invitations.length > 0 &&
                                <>
                                    <Typography variant={"h5"} component={"h2"} sx={{marginBottom: 2}}>Friend
                                        requests</Typography>
                                    <PeopleTable
                                        data={invitations}
                                        setData={handleUpdateInvitations}
                                        label="requests"
                                    />
                                </>
                            }
                            {
                                friends.length > 0 &&
                                <>
                                    <Typography variant={"h5"} component={"h2"} sx={{marginTop: 2, marginBottom: 2}}>My
                                        friends</Typography>
                                    <PeopleTable
                                        data={friends}
                                        setData={setFriends}
                                        label="friends"
                                    />
                                </>
                            }
                            <TablePagination
                                onPreviousClick={() => setPage(page - 1)}
                                onNextClick={() => setPage(page + 1)}
                                hasPreviousValues={hasPreviousPage}
                                hasNextValues={hasLastPage}
                                isNextButtonLoading={isButtonLoading}
                                isPreviousButtonLoading={isButtonLoading}
                            />
                        </>
                    )
            }
        </TableContainer>

    )
}
