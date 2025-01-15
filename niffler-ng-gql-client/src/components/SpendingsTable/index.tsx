import {
    Box,
    Checkbox,
    IconButton,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableRow,
    Typography,
    useMediaQuery,
    useTheme
} from "@mui/material";
import {TablePagination} from "../Table/Pagination";
import {ChangeEvent, FC, useCallback, useEffect, useState} from "react";
import {convertCurrencyToData, Currency, CurrencyValue, getCurrencyIcon} from "../../types/Currency.ts";
import {TableHead} from "../Table/TableHead";
import {HeadCell} from "../Table/HeadCell";
import {convertDate} from "../../utils/date.ts";
import EditIcon from "../../assets/icons/ic_edit.svg?react";
import {useNavigate} from "react-router-dom";
import {EmptyTableState} from "../EmptyUsersState";
import {FilterPeriodValue} from "../../types/FilterPeriod.ts";
import {convertFilterPeriod} from "../../utils/dataConverter.ts";
import {Loader} from "../Loader";
import {Toolbar} from "./Toolbar";
import {useSnackBar} from "../../context/SnackBarContext.tsx";
import {useCurrenciesQuery, useSpendsQuery} from "../../generated/graphql.tsx";

const headCells: readonly HeadCell[] = [
    {
        id: "category",
        position: "left",
        label: "Category",
    },
    {
        id: "amount",
        position: "center",
        label: "Amount",
    },
    {
        id: "description",
        position: "center",
        label: "Description",
    },
    {
        id: "date",
        position: "center",
        label: "Date",
    },
    {
        id: "actions",
        position: "center",
        label: "",
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
    const [currencies, setCurrencies] = useState<Currency[]>([]);
    const [search, setSearch] = useState("");
    const [selected, setSelected] = useState<string[]>([]);
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('md'));
    const navigate = useNavigate();
    const snackBar = useSnackBar();
    const {data: spends, refetch, loading} = useSpendsQuery({
        variables: {
            page,
            size: 10,
            sort: ["spendDate,ASC", "amount,DESC"],
            searchQuery: search,
            filterPeriod: convertFilterPeriod(period),
            filterCurrency: convertCurrencyToData(selectedCurrency),
        },
        fetchPolicy: "cache-and-network",
        errorPolicy: "none",
        onError: (err) => {
            console.error(err);
            snackBar.showSnackBar("Error while loading spends data", "error");
        }
    });
    useCurrenciesQuery({
        errorPolicy: "none",
        onError: (err) => {
            console.error(err);
            snackBar.showSnackBar("Can not load currencies", "error");
        },
        onCompleted: (data) => {
            setCurrencies([
                {
                    currency: "ALL",
                },
                ...data.currencies
            ]);
        }
    });

    const loadSpends = useCallback((search: string, page: number, period: FilterPeriodValue, selectedCurrency: Currency) => {
        refetch({
            page,
            size: 10,
            sort: ["spendDate,ASC", "amount,DESC"],
            searchQuery: search,
            filterPeriod: convertFilterPeriod(period),
            filterCurrency: convertCurrencyToData(selectedCurrency),
        });
    }, [refetch]);

    useEffect(() => {
        loadSpends(search, page, period, selectedCurrency);
    }, [period, selectedCurrency, search, page, loadSpends]);

    const handleSpendingClick = (id: string) => {
        navigate(`/spending/${id}`);
    }

    const handleSelectAllClick = (event: ChangeEvent<HTMLInputElement>) => {
        if (event.target.checked && spends?.spends?.edges) {
            const newSelected: string[] = spends.spends.edges
                .filter(n => n !== null)
                .map((n) => n.node.id);
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

    const handleInputSearch = (value: string) => {
        setSearch(value);
        setPage(0);
    }

    const onDelete = () => {
        onDeleteCallback();
        loadSpends(search, page, period, selectedCurrency);
        setSelected([]);
    }

    return (
        <TableContainer sx={{
            maxWidth: 900,
            margin: "0 auto",
        }}>

            <Toolbar
                handleInputSearch={handleInputSearch}
                period={period}
                selectedCurrency={selectedCurrency}
                handleChangeCurrency={handleChangeCurrency}
                handleChangePeriod={handleChangePeriod}
                selectedSpendIds={selected}
                currencies={currencies}
                onDeleteCallback={onDelete}
            />

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
                    ) : spends?.spends?.edges.length ? (
                        <>
                            <Table
                                aria-labelledby="tableTitle"
                                sx={{marginBottom: "12px"}}
                            >
                                {!isMobile &&
                                    <TableHead
                                        headCells={headCells}
                                        onSelectAllClick={handleSelectAllClick}
                                        numSelected={selected.length}
                                        rowCount={spends.spends.edges.length}/>
                                }
                                <TableBody>
                                    {
                                        spends.spends.edges.map((row) => {
                                            if (row) {
                                                const isItemSelected = isSelected(row.node.id);
                                                const labelId = `enhanced-table-checkbox-${row.node.id}`;

                                                return (
                                                    <TableRow
                                                        hover
                                                        onClick={(event) => handleRowClick(event, row.node.id)}
                                                        role="checkbox"
                                                        aria-checked={isItemSelected}
                                                        tabIndex={-1}
                                                        key={row.node.id}
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
                                                        {
                                                            isMobile
                                                                ?
                                                                <>
                                                                    <TableCell
                                                                        component="td"
                                                                        id={labelId}
                                                                        scope="row"
                                                                        padding="none"
                                                                        align="left"
                                                                    >
                                                                        <Typography component="span" variant={"body1"}>
                                                                            <Stack>{row.node.category.name}</Stack>
                                                                            <Stack
                                                                                sx={{color: theme.palette.gray_600.main}}>{row.node.description}</Stack>
                                                                        </Typography>
                                                                    </TableCell>
                                                                    <TableCell align="right" padding={"normal"}
                                                                               sx={{minWidth: 110}}>
                                                                        <Typography component="span" variant={"body1"}>
                                                                            <Stack>
                                                                                `${row.node.amount} ${getCurrencyIcon(row.node.currency as CurrencyValue)}` </Stack>
                                                                            <Stack
                                                                                sx={{color: theme.palette.gray_600.main}}>{convertDate(row.node.spendDate)}</Stack>
                                                                        </Typography>
                                                                    </TableCell>
                                                                </>
                                                                :
                                                                <>
                                                                    <TableCell
                                                                        component="td"
                                                                        id={labelId}
                                                                        scope="row"
                                                                        padding="none"
                                                                        align="left"
                                                                    >
                                                                        <Typography component="span"
                                                                                    variant={"body1"}>{row.node.category.name}</Typography>
                                                                    </TableCell>
                                                                    <TableCell align="center" padding={"normal"}>
                                                                        <Typography component="span"
                                                                                    variant={"body1"}> {
                                                                            `${row.node.amount} ${getCurrencyIcon(row.node.currency as CurrencyValue)}`
                                                                        }</Typography>
                                                                    </TableCell>
                                                                    <TableCell align="center"
                                                                               padding={"normal"}
                                                                               sx={{
                                                                                   color: theme.palette.gray_600.main,
                                                                               }}
                                                                    >
                                                                        <Typography component="span"
                                                                                    variant={"body1"}>{row.node.description}</Typography>
                                                                    </TableCell>
                                                                    <TableCell align="center"
                                                                               padding={"normal"}
                                                                               sx={{
                                                                                   minWidth: 110,
                                                                                   color: theme.palette.gray_600.main
                                                                               }}>
                                                                        <Typography component="span"
                                                                                    variant={"body1"}>{convertDate(row.node.spendDate)}</Typography>
                                                                    </TableCell>
                                                                </>
                                                        }


                                                        <TableCell align="right" padding={"normal"}>
                                                            <IconButton color="primary" aria-label="Edit spending"
                                                                        onClick={(e) => {
                                                                            e.stopPropagation();
                                                                            handleSpendingClick(row.node.id);
                                                                        }}>
                                                                <EditIcon color={theme.palette.gray_600.main}/>
                                                            </IconButton>
                                                        </TableCell>
                                                    </TableRow>
                                                );
                                            }
                                        })}
                                </TableBody>
                            </Table>
                            <TablePagination
                                isNextButtonLoading={loading}
                                isPreviousButtonLoading={loading}
                                onPreviousClick={() => setPage(page - 1)}
                                onNextClick={() => setPage(page + 1)}
                                hasPreviousValues={spends?.spends?.pageInfo.hasPreviousPage ?? false}
                                hasNextValues={spends?.spends?.pageInfo.hasNextPage ?? false}
                            />
                        </>
                    ) : (
                        <EmptyTableState title={"There are no spendings"}/>
                    )
            }
        </TableContainer>
    )
}
