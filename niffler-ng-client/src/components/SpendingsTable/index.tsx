import {
    Box, Checkbox,
    IconButton,
    InputBase,
    MenuItem,
    Table,
    TableBody, TableCell,
    TableContainer, TableRow,
    TextField,
    Toolbar,
    useTheme
} from "@mui/material";
import {TablePagination} from "../Table/Pagination";
import {ChangeEvent, useEffect, useState} from "react";
import SearchIcon from "@mui/icons-material/Search";
import {SecondaryButton} from "../Button";
import {filterPeriods} from "../../const/filterPeriods.ts";
import {apiClient} from "../../api/apiClient.ts";
import {Currency} from "../../types/Currency.ts";
import {Icon} from "../Icon";
import {TableHead} from "../Table/TableHead";
import {HeadCell} from "../Table/HeadCell";
import {Spending} from "../../types/Spending.ts";
import {convertDate} from "../../utils/date.ts";
import EditIcon from "../../assets/icons/ic_edit.svg?react";
import {useNavigate} from "react-router-dom";


const headCells: readonly HeadCell[] = [
    {
        id: 'category',
        numeric: true,
        label: 'Category',
    },
    {
        id: 'amount',
        numeric: false,
        label: 'Amount',
    },
    {
        id: 'description',
        numeric: false,
        label: 'Description',
    },
    {
        id: 'date',
        numeric: false,
        label: 'Date',
    },
    {
        id: 'actions',
        numeric: false,
        label: '',
    }
];

export const SpendingsTable = () => {
    const [page, setPage] = useState(0);
    const [search, setSearch] = useState("");
    const [currencies, setCurrencies] = useState<Currency[]>([]);
    const [selected, setSelected] = useState<readonly number[]>([]);
    const [data, setData] = useState<readonly Spending[]>([]);
    const theme = useTheme();
    const navigate = useNavigate();

    useEffect(() => {
        apiClient.getCurrencies({
           onSuccess: (data) => {
               setCurrencies(data);
           },
           onFailure: (e) => console.log(e),
        });

        apiClient.getStatistics({
            onSuccess: (data) => {
               console.log(data);
           },
            onFailure: (e) => console.log(e),
        });

        apiClient.getSpends({
            onSuccess: (data) => {
                console.log(data);
                const converted = data.content.map(item => ({
                    id: item.id,
                    description: item.description,
                    amount: `${item.amount} ${item.currency}`,
                    category: item.category,
                    spendDate: convertDate(item.spendDate),
                }));
                setData(converted);
            },
            onFailure: (e) => console.log(e),
        });
    }, []);

    const hasPreviousPage = false;
    const hasNextPage = true;

    const handleSpendingClick = (id: string) => {
        console.log(id);
        navigate(`/spending/${id}`);
    }

    const handleSelectAllClick = (event: ChangeEvent<HTMLInputElement>) => {
        if (event.target.checked) {
            const newSelected = rows.map((n) => n.id);
            setSelected(newSelected);
            return;
        }
        setSelected([]);
    };

    const isSelected = (id: number) => selected.indexOf(id) !== -1;



    return (
        <TableContainer sx={{
            maxWidth: 900,
            margin: "0 auto",
        }}>
            <Toolbar>
                <Box component="form" onSubmit={() => {}} sx={{display: "flex", width: "100%"}}>
                    <Box sx={{ display: 'flex', alignItems: 'center', width: "100%", backgroundColor: theme.palette.secondary.light, border: "1px solid #E4E6F1", borderRadius: "8px" }}>
                        <InputBase
                            sx={{ ml: 1, flex: 1}}
                            placeholder="Search"
                            value={search}
                            onChange={(e) => setSearch(e.target.value)}
                            inputProps={{ 'aria-label': 'search people' }}
                        />
                        <IconButton type="submit" sx={{ p: '10px' }} aria-label="search" color={"primary"}>
                            <SearchIcon />
                        </IconButton>
                    </Box>
                    <TextField
                        sx={{
                            margin: "0 8px",
                            padding: 0 ,
                            maxWidth: "140px",
                            border: "none",
                        }}
                        id="period"
                        name="period"
                        type="text"
                        select
                        defaultValue={"ALL"}
                        error={false}
                        fullWidth
                    >
                        {filterPeriods.map((option) => (
                            <MenuItem key={option.value} value={option.value}>
                                {option.label}
                            </MenuItem>
                        ))}
                    </TextField>
                    <TextField
                        sx={{
                            margin: "0 8px",
                            padding: 0,
                            maxWidth: "100px"
                        }}
                        id="currency"
                        name="currency"
                        type="text"
                        select
                        defaultValue={"RUB"}
                        error={false}
                        fullWidth
                    >
                        {currencies.map((option) => (
                            <MenuItem key={option.currency} value={option.currency}>
                                {option.currency}
                            </MenuItem>
                        ))}
                    </TextField>

                </Box>
                <SecondaryButton
                    startIcon={<Icon type="deleteIcon"/>}
                >
                    Delete
                </SecondaryButton>
            </Toolbar>
            <Table
                aria-labelledby="tableTitle"
            >
                <TableHead headCells={headCells} onSelectAllClick={handleSelectAllClick} numSelected={0} rowCount={10}/>
                <TableBody>
                    {data.map((row) => {
                        const isItemSelected = isSelected(row.id);
                        const labelId = `enhanced-table-checkbox-${row.id}`;

                        return (
                            <TableRow
                                hover
                                onClick={(event) => handleClick(event, row.id)}
                                role="checkbox"
                                aria-checked={isItemSelected}
                                tabIndex={-1}
                                key={row.id}
                                selected={isItemSelected}
                                sx={{ cursor: 'pointer' }}
                            >
                                <TableCell padding="checkbox">
                                    <Checkbox
                                        color="primary"
                                        checked={isItemSelected}
                                        inputProps={{
                                            'aria-labelledby': labelId,
                                        }}
                                    />
                                </TableCell>
                                <TableCell
                                    component="th"
                                    id={labelId}
                                    scope="row"
                                    padding="none"
                                >
                                    {row.category.category}
                                </TableCell>
                                <TableCell align="right">{row.amount}</TableCell>
                                <TableCell align="right">{row.description}</TableCell>
                                <TableCell align="right">{row.spendDate}</TableCell>
                                <TableCell align="right">
                                    <IconButton color="primary" aria-label="Edit spending" onClick={(e) => {
                                        e.stopPropagation();
                                        handleSpendingClick(row.id);
                                    }}>
                                        <EditIcon color={theme.palette.gray_600.main}/>
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        );
                    })}
                </TableBody>
            </Table>



            <TablePagination
                onPreviousClick={() => setPage(page - 1)}
                onNextClick={() => setPage(page + 1)}
                hasPreviousValues={hasPreviousPage}
                hasNextValues={hasNextPage}
            />
        </TableContainer>
    )
}