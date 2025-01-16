import {Avatar, Box, Table, TableBody, TableCell, TableRow, Typography, useTheme} from "@mui/material";
import {TableUser} from "../../types/User";
import {FC} from "react";
import {ActionButtons} from "./ActionButtons";


interface PeopleTableInterface {
    data: TableUser[];
    onUpdateCallback: () => void;
    label: string;
}

export const PeopleTable: FC<PeopleTableInterface> = ({
                                                          data,
                                                          onUpdateCallback,
                                                          label,

                                                      }) => {

    const theme = useTheme();
    return (
        <>
            <Table
                aria-labelledby="tableTitle"
                sx={{marginBottom: "12px"}}
            >
                {data?.length > 0 && (
                    <TableBody id={label}>
                        {data.map((row: TableUser) => {
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
                                            src={row.photoSmall || ""}
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
                                            onUpdateCallback={onUpdateCallback}
                                            friendshipStatus={row.friendshipStatus}
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
