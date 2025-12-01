import {Checkbox, TableCell, TableHead as MuiTableHead, TableRow, Typography, useTheme} from "@mui/material";
import {HeadCell} from "../HeadCell";
import {ChangeEvent, FC} from "react";

interface TableHeadProps {
    headCells: readonly HeadCell[];
    onSelectAllClick: (event: ChangeEvent<HTMLInputElement>) => void;
    numSelected: number;
    rowCount: number;
}

export const TableHead: FC<TableHeadProps> = ({headCells, onSelectAllClick, numSelected, rowCount}) => {
    const theme = useTheme();

    return (
        <MuiTableHead sx={{
            backgroundColor: theme.palette.secondary.light,
        }}>
            <TableRow>
                <TableCell padding="checkbox">
                    <Checkbox
                        color="primary"
                        indeterminate={numSelected > 0 && numSelected < rowCount}
                        checked={rowCount > 0 && numSelected === rowCount}
                        onChange={onSelectAllClick}
                        inputProps={{
                            "aria-label": "select all rows",
                        }}
                    />
                </TableCell>
                {headCells.map((headCell) => (
                    <TableCell
                        key={headCell.id}
                        align={headCell.position}
                        padding={'normal'}
                    >
                        <Typography component="p" variant={"body1"}>
                            {headCell.label}
                        </Typography>
                    </TableCell>
                ))}
            </TableRow>
        </MuiTableHead>
    );
}
