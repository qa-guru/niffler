import {Box, Menu, MenuItem, Stack, TextField, Toolbar as MuiToolbar, useMediaQuery, useTheme} from "@mui/material";
import FileDownloadOutlinedIcon from "@mui/icons-material/FileDownloadOutlined";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import UploadFileOutlinedIcon from "@mui/icons-material/UploadFileOutlined";
import {SearchInput} from "../../SearchInput";
import {filterPeriod, FilterPeriodValue} from "../../../types/FilterPeriod.ts";
import {Currency, CurrencyValue, getCurrencyIcon} from "../../../types/Currency.ts";
import {SecondaryButton} from "../../Button";
import {ChangeEvent, FC, MouseEvent, useState} from "react";
import {apiClient} from "../../../api/apiClient.ts";
import {useDialog} from "../../../context/DialogContext.tsx";
import {useSnackBar} from "../../../context/SnackBarContext.tsx";
import DeleteIcon from "../../../assets/icons/ic_delete.svg?react";


interface ToolbarInterface {
    handleInputSearch: (value: string) => void;
    search: string,
    onSearchChange: (value: string) => void,
    period: FilterPeriodValue,
    handleChangePeriod: (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void,
    selectedCurrency: Currency,
    handleChangeCurrency: (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void,
    currencies: Currency[],
    selectedSpendIds: string[],
    onDeleteCallback: () => void,
}

export const Toolbar: FC<ToolbarInterface> = ({
                                                  handleInputSearch,
                                                  search,
                                                  onSearchChange,
                                                  period,
                                                  handleChangePeriod,
                                                  selectedCurrency,
                                                  handleChangeCurrency,
                                                  currencies,
                                                  selectedSpendIds,
                                                  onDeleteCallback
                                              }) => {
    const theme = useTheme();
    const dialog = useDialog();
    const snackbar = useSnackBar();
    const isMobile = useMediaQuery(theme.breakpoints.down('md'));
    const [menuAnchor, setMenuAnchor] = useState<null | HTMLElement>(null);
    const isMenuOpen = Boolean(menuAnchor);

    const handleOpenMenu = (event: MouseEvent<HTMLButtonElement>) => {
        setMenuAnchor(event.currentTarget);
    }

    const handleCloseMenu = () => {
        setMenuAnchor(null);
    }

    const downloadCsv = (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.download = "spend-history.csv";
        link.click();
        window.URL.revokeObjectURL(url);
    }

    const handleExportCsv = () => {
        handleCloseMenu();
        apiClient.exportSpendsToCsv({
            onSuccess: (csv) => {
                downloadCsv(csv);
                snackbar.showSnackBar("Spendings successfully exported", "info");
            },
            onFailure: (e) => {
                snackbar.showSnackBar("Can not export spendings", "error");
                console.error(e.message);
            },
        });
    }

    const onDeleteButtonClick = () => {
        dialog.showDialog({
            title: "Delete spendings?",
            description: "If you are sure, submit your action.",
            onSubmit: () => {
                apiClient.deleteSpends(selectedSpendIds, {
                    onSuccess: () => {
                        snackbar.showSnackBar("Spendings succesfully deleted", "info");
                        onDeleteCallback();
                    },
                    onFailure: (e) => {
                        snackbar.showSnackBar("Can not delete spendings", "error");
                        console.error(e.message);
                    }
                });
            },
            submitTitle: "Delete",
            closeTitle: "Cancel"
        });
    }

    return (
        <MuiToolbar sx={{
            display: "flex",
            flexDirection: isMobile ? "column" : "row",
        }}>
            <SearchInput onSearchSubmit={handleInputSearch} value={search} onChange={onSearchChange}/>
            <Box sx={{
                display: "flex",
                width: "100%",
                margin: isMobile ? 2 : 0,
                justifyContent: "space-between",
            }}>
                <TextField
                    sx={{
                        margin: isMobile ? "0 8px 0 0" : "0 8px",
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
                            margin: 0,
                            marginRight: "8px",
                            padding: 0,
                            maxWidth: "120px"
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
                                <Stack sx={{fontSize: 18, display: "inline"}} component="span">
                                    {getCurrencyIcon(option.currency as CurrencyValue)}
                                </Stack>
                                &nbsp;&nbsp;
                                <Stack sx={{color: theme.palette.gray_600.main, display: "inline"}} component="span">
                                    {option.currency}
                                </Stack>
                            </MenuItem>
                        ))}
                    </TextField>
                )}
                <SecondaryButton
                    sx={{
                        fontWeight: 400,
                        padding: 1,
                        minWidth: "48px",
                        minHeight: "48px",
                        marginRight: "8px",
                    }}
                    id="spending-menu"
                    type={"button"}
                    aria-controls={isMenuOpen ? "spending-csv-menu" : undefined}
                    aria-haspopup="true"
                    aria-expanded={isMenuOpen ? "true" : undefined}
                    onClick={handleOpenMenu}
                >
                    <MoreVertIcon/>
                </SecondaryButton>
                <Menu
                    id="spending-csv-menu"
                    anchorEl={menuAnchor}
                    open={isMenuOpen}
                    onClose={handleCloseMenu}
                >
                    <MenuItem onClick={handleExportCsv}>
                        <FileDownloadOutlinedIcon sx={{marginRight: 1}}/>
                        Export to CSV
                    </MenuItem>
                    <MenuItem disabled>
                        <UploadFileOutlinedIcon sx={{marginRight: 1}}/>
                        Import CSV
                    </MenuItem>
                </Menu>
                {isMobile ?
                    <SecondaryButton
                        sx={{
                            fontWeight: 400,
                            padding: 1,
                            minWidth: "48px",
                            minHeight: "48px",
                        }}
                        id="delete"
                        type={"button"}
                        onClick={onDeleteButtonClick}
                        disabled={selectedSpendIds.length === 0}
                    >
                        <DeleteIcon/>
                    </SecondaryButton>
                    :
                    <SecondaryButton
                        sx={{
                            fontWeight: 400,
                            minWidth: 100
                        }}
                        id="delete"
                        type={"button"}
                        startIcon={<DeleteIcon/>}
                        onClick={onDeleteButtonClick}
                        disabled={selectedSpendIds.length === 0}
                    >
                        Delete
                    </SecondaryButton>
                }
            </Box>
        </MuiToolbar>
    )
}
