import {Avatar, Box, Table, TableBody, TableCell, TableRow, Typography, useTheme} from "@mui/material";
import {User} from "../../types/User";
import {FC} from "react";
import {ActionButtons} from "./ActionButtons";


interface PeopleTableInterface {
    data: User[];
    setData: (data: User[]) => void;
    label: string;
}

export const PeopleTable: FC<PeopleTableInterface> = ({
                                                          data,
                                                          setData,
                                                          label,
                                                      }) => {

    const theme = useTheme();

    const handleUpdateUserData = (username: string, newFriendshipStatus: "FRIEND" | "INVITE_SENT" | "INVITE_RECEIVED" | undefined) => {
        const index = data.findIndex(user => user.username === username);
        if (index === -1) return data;

        if (!newFriendshipStatus) {
            if (data.length === 1) {
                setData([]);
            } else {
                setData([
                    ...data.slice(0, index),
                    ...data.slice(index + 1)
                ]);
            }
        } else {
            setData([
                ...data.slice(0, index),
                {...data[index], friendshipStatus: newFriendshipStatus},
                ...data.slice(index + 1)
            ]);
        }
    };

    return (
        <>
            <Table
                aria-labelledby="tableTitle"
                sx={{marginBottom: "12px"}}
            >
                {data?.length > 0 && (
                    <TableBody id={label}>
                        {data.map((row: User) => {
                            return (
                                <TableRow
                                    hover
                                    tabIndex={-1}
                                    key={row.id}
                                    sx={{
                                        borderColor: "transparent",
                                        outlineColor: "transparent"
                                    }}
                                >
                                    <TableCell sx={{
                                        display: "flex",
                                        alignItems: "center",
                                        padding: "4px 0",
                                    }}>
                                        <Avatar
                                            sx={{
                                                margin: 1,
                                                marginRight: 3,
                                                width: 48,
                                                height: 48,
                                            }}
                                            src={row.photoSmall}
                                        />
                                        <Box>
                                            <Typography variant="body1" component="p">{row.username}</Typography>
                                            <Typography variant="body2" component="p"
                                                        sx={{color: theme.palette.gray_600.main}}>{row.fullname}</Typography>
                                        </Box>
                                    </TableCell>
                                    <TableCell align="right" sx={{padding: "4px 0"}}
                                    >
                                        <ActionButtons
                                            username={row.username}
                                            friendshipStatus={row.friendshipStatus}
                                            handleUpdateUserData={handleUpdateUserData}
                                        />
                                    </TableCell>
                                </TableRow>
                            );
                        })}
                    </TableBody>)
                }
            </Table>
        </>
    )
}
