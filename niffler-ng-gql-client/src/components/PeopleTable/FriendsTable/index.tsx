import {useCallback, useEffect, useState} from "react";
import {PeopleTable} from "..";
import {TableToolbar} from "../../Table/TableToolbar";
import {Box, TableContainer, Typography} from "@mui/material";
import {TableUser} from "../../../types/User.ts";
import {TablePagination} from "../../Table/Pagination";
import {EmptyTableState} from "../../EmptyUsersState";
import {Loader} from "../../Loader";
import {useSnackBar} from "../../../context/SnackBarContext.tsx";
import {FriendshipStatus, useFriendsQuery} from "../../../generated/graphql.tsx";

export const FriendsTable = () => {
    const [page, setPage] = useState(0);
    const [search, setSearch] = useState("");
    const [friends, setFriends] = useState<TableUser[]>([]);
    const [invitations, setInvitations] = useState<TableUser[]>([]);
    const snackbar = useSnackBar();
    const {data, loading, refetch} = useFriendsQuery({
        variables: {
            page,
            size: 10,
            searchQuery: search,
        },
        errorPolicy: "none",
        fetchPolicy: "cache-and-network",
        onCompleted: (data) => {
            setFriends(data.user.friends?.edges
                    .filter(edge => edge !== null)
                    .filter(user => user.node.friendshipStatus === FriendshipStatus.Friend)
                    .map(user => user.node)
                ?? []);
            setInvitations(data.user.friends?.edges
                    .filter(edge => edge !== null)
                    .filter(user => user.node.friendshipStatus === FriendshipStatus.InviteReceived)
                    .map(user => user.node)
                ?? []);
        },
        onError: (err) => {
            console.error(err);
            snackbar.showSnackBar("Error while loading friends data", "error");
        }
    });

    const loadFriends = useCallback(() => {
        refetch({
            page,
            size: 10,
            searchQuery: search,
        });
    }, [refetch, search, page]);

    useEffect(() => {
        loadFriends();
    }, [loadFriends]);


    const handleInputSearch = (value: string) => {
        setSearch(value);
        setPage(0);
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
            {loading
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
                                        label="requests"
                                        onUpdateCallback={loadFriends}
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
                                        onUpdateCallback={loadFriends}
                                        label="friends"
                                    />
                                </>
                            }
                            <TablePagination
                                onPreviousClick={() => setPage(page - 1)}
                                onNextClick={() => setPage(page + 1)}
                                hasPreviousValues={data?.user.friends?.pageInfo.hasPreviousPage ?? false}
                                hasNextValues={data?.user.friends?.pageInfo.hasNextPage ?? false}
                                isNextButtonLoading={loading}
                                isPreviousButtonLoading={loading}
                            />
                        </>
                    )
            }
        </TableContainer>

    )
}
