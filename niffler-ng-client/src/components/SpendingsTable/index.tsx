import {
    Box,
    Checkbox,
    IconButton,
    MenuItem,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableRow,
    TextField,
    Toolbar,
    useTheme
} from "@mui/material";
import {TablePagination} from "../Table/Pagination";
import {ChangeEvent, FC, useEffect, useState} from "react";
import {SecondaryButton} from "../Button";
import {apiClient} from "../../api/apiClient.ts";
import {convertCurrencyToData, Currency} from "../../types/Currency.ts";
import {Icon} from "../Icon";
import {TableHead} from "../Table/TableHead";
import {HeadCell} from "../Table/HeadCell";
import {TableSpending} from "../../types/Spending.ts";
import {convertDate} from "../../utils/date.ts";
import EditIcon from "../../assets/icons/ic_edit.svg?react";
import {useNavigate} from "react-router-dom";
import {EmptyTableState} from "../EmptyUsersState";
import {useSnackBar} from "../../context/SnackBarContext.tsx";
import {filterPeriod, FilterPeriodValue} from "../../types/FilterPeriod.ts";
import {convertFilterPeriod} from "../../utils/dataConverter.ts";
import {SearchInput} from "../SearchInput";
import {Loader} from "../Loader";


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
        numeric: true,
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

interface SpendingsTableInterface {
    period: FilterPeriodValue,
    handleChangePeriod: (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void,
    selectedCurrency: Currency,
    handleChangeCurrency: (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void,
    onDeleteCallback: () => void;
}

export const SpendingsTable: FC<SpendingsTableInterface> = ({
                                                                period,
                                                                handleChangePeriod,
                                                                selectedCurrency,
                                                                handleChangeCurrency,
                                                                onDeleteCallback
                                                            }) => {
    const [page, setPage] = useState(0);
    const [hasPreviousPage, setHasPreviousPage] = useState(false);
    const [hasLastPage, setHasLastPage] = useState(false);
    const [currencies, setCurrencies] = useState<Currency[]>([]);
    const [search, setSearch] = useState("");
    const [selected, setSelected] = useState<string[]>([]);
    const [data, setData] = useState<readonly TableSpending[]>([]);
    const [loading, setLoading] = useState(false);
    const theme = useTheme();
    const navigate = useNavigate();
    const snackbar = useSnackBar();

    const loadSpends = (search: string, page: number, period: FilterPeriodValue, filterCurrency: Currency) => {
        setLoading(true);
        apiClient.getSpends(
            search, page, {
                onSuccess: (data) => {
                    const converted = data.content.map(item => ({
                        id: item.id,
                        description: item.description,
                        amount: `${item.amount} ${item.currency}`,
                        category: item.category,
                        spendDate: convertDate(item.spendDate),
                    }));
                    setData(converted);
                    setHasPreviousPage(!data.first);
                    setHasLastPage(!data.last);
                    setLoading(false);
                },
                onFailure: (e) => {
                    setLoading(false);
                    console.error(e.message);
                },
            },
            convertFilterPeriod(period),
            convertCurrencyToData(filterCurrency)
        );
    }

    useEffect(() => {
        apiClient.getCurrencies({
            onSuccess: (data) => {
                setCurrencies([
                    {
                        currency: "ALL",
                    },
                    ...data
                ]);
            },
            onFailure: (e) => console.error(e.message),
        });
    }, []);

    useEffect(() => {
        loadSpends(search, page, period, selectedCurrency);
    }, [period, selectedCurrency, search, page]);

    const handleSpendingClick = (id: string) => {
        navigate(`/spending/${id}`);
    }

    const handleSelectAllClick = (event: ChangeEvent<HTMLInputElement>) => {
        if (event.target.checked) {
            const newSelected = data.map((n) => n.id);
            setSelected(newSelected);
            return;
        }
        setSelected([]);
    };

    const handleRowClick = (_event: React.MouseEvent<unknown>, id: string) => {
        const selectedIndex = selected.indexOf(id);
        let newSelected: string[] = [];

        if (selectedIndex === -1) {
            newSelected = newSelected.concat(selected, id);
        } else {
            newSelected = newSelected.concat(selected
                .slice(0, selectedIndex)
                .concat(selected.slice(selectedIndex + 1)));
        }
        setSelected(newSelected);
    };

    const isSelected = (id: string) => selected.indexOf(id) !== -1;


    const onDeleteButtonClick = () => {
        apiClient.deleteSpends(selected, {
            onSuccess: () => {
                snackbar.showSnackBar("Spendings succesfully deleted", "info");
                onDeleteCallback();
                loadSpends(search, page, period, selectedCurrency);
                setSelected([]);
            },
            onFailure: (e) => {
                snackbar.showSnackBar("Can not delete spendings", "error");
                console.error(e.message);
            }
        });
    }

    const handleInputSearch = (value: string) => {
        setSearch(value);
        setPage(0);
    }


    return (
        <TableContainer sx={{
            maxWidth: 900,
            margin: "0 auto",
        }}>
            <Toolbar>
                <Box sx={{display: "flex", width: "100%"}}>
                    <SearchInput onSearchSubmit={handleInputSearch}/>
                    <TextField
                        sx={{
                            margin: "0 8px",
                            padding: 0,
                            maxWidth: "140px",
                            border: "none",
                        }}
                        id="period"
                        name="period"
                        type="text"
                        select
                        value={period.value}
                        onChange={e => handleChangePeriod(e)}
                        error={false}
                        fullWidth
                    >
                        {filterPeriod.map((option) => (
                            <MenuItem key={option.value} value={option.value}>
                                {option.label}
                            </MenuItem>
                        ))}
                    </TextField>
                    {currencies?.length > 0 && (
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
                            error={false}
                            value={selectedCurrency.currency}
                            onChange={handleChangeCurrency}
                            fullWidth
                        >
                            {currencies.map((option) => (
                                <MenuItem key={option.currency} value={option.currency}>
                                    {option.currency}
                                </MenuItem>
                            ))}
                        </TextField>
                    )}
                </Box>
                <SecondaryButton
                    type={"button"}
                    startIcon={<Icon type="deleteIcon"/>}
                    onClick={onDeleteButtonClick}
                    disabled={selected.length === 0}
                >
                    Delete
                </SecondaryButton>
            </Toolbar>
            {
                loading ?
                    (
                        <Box sx={{
                            position: "relative",
                            width: "100%",
                            minHeight: 150,
                        }}>
                            <Loader/>
                        </Box>
                    ) : data.length > 0 ? (
                        <>
                            <Table
                                aria-labelledby="tableTitle"
                            >
                                <TableHead
                                    headCells={headCells}
                                    onSelectAllClick={handleSelectAllClick}
                                    numSelected={selected.length}
                                    rowCount={data.length}/>
                                <TableBody>
                                    {data.map((row) => {
                                        const isItemSelected = isSelected(row.id);
                                        const labelId = `enhanced-table-checkbox-${row.id}`;

                                        return (
                                            <TableRow
                                                hover
                                                onClick={(event) => handleRowClick(event, row.id)}
                                                role="checkbox"
                                                aria-checked={isItemSelected}
                                                tabIndex={-1}
                                                key={row.id}
                                                selected={isItemSelected}
                                                sx={{cursor: 'pointer'}}
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
                                                    {row.category.name}
                                                </TableCell>
                                                <TableCell align="right" padding={"normal"}>{row.amount}</TableCell>
                                                <TableCell align="left" padding={"normal"}>{row.description}</TableCell>
                                                <TableCell align="right" padding={"normal"} sx={{minWidth: 110}}>{row.spendDate}</TableCell>
                                                <TableCell align="right" padding={"normal"}>
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
                                hasNextValues={hasLastPage}
                            />
                        </>
                    ) : (
                        <EmptyTableState title={"There are no spendings yet"}/>
                    )
            }


        </TableContainer>
    )
}
