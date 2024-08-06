import {Avatar, Box, Table, TableBody, TableCell, TableContainer, TableRow, Typography, useTheme} from "@mui/material";
import {User} from "../../types/User";
import {FC} from "react";
import {ActionButtons} from "../Table/ActionButtons";


interface PeopleTableInterface {
    data: User[];
    setData: (data: User[]) => void;
}

export const PeopleTable: FC<PeopleTableInterface> = ({
                                                          data,
                                                          setData,

                                                      }) => {

    const theme = useTheme();

    const handleUpdateUserData = (username: string, newFriendState: string) => {

    }

    return (
        <>
            <Table aria-labelledby="tableTitle"
            >
                {data?.length > 0 && (
                    <TableBody>
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
                                            <Typography variant="body2" component="p" sx={{color: theme.palette.gray_600.main}}>{row.fullname}</Typography>
                                        </Box>
                                    </TableCell>
                                    <TableCell align="right">
                                        <ActionButtons username={row.username} friendState={row.friendState}/>
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