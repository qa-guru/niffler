import {Box, Grid, Typography, useMediaQuery, useTheme} from "@mui/material";
import {SpendingsTable} from "../../components/SpendingsTable";
import {ChangeEvent, useCallback, useEffect, useState} from "react";
import {convertValueToFilterPeriodValue, FilterPeriodValue} from "../../types/FilterPeriod.ts";
import {convertFilterPeriod} from "../../utils/dataConverter.ts";
import {convertCurrencyToData, Currency} from "../../types/Currency.ts";
import {Diagram} from "../../components/Diagram";
import {useStatQuery} from "../../generated/graphql.tsx";
import {useSnackBar} from "../../context/SnackBarContext.tsx";

export const MainPage = () => {
    const [period, setPeriod] = useState<FilterPeriodValue>({label: "All time", value: "ALL"});
    const [selectedCurrency, setSelectedCurrency] = useState<Currency>(
        {currency: "ALL"});
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const shouldRecise = useMediaQuery(theme.breakpoints.down('md'));
    const snackbar = useSnackBar();

    const {data, refetch} = useStatQuery({
        variables: {
            statCurrency: convertCurrencyToData(selectedCurrency),
            filterCurrency: convertCurrencyToData(selectedCurrency),
            filterPeriod: convertFilterPeriod(period),
        },
        errorPolicy: "none",
        onError: (err) => {
            console.error(err);
            snackbar.showSnackBar("Error while loading stats", "error");
        }
    });

    const loadStats = useCallback(() => {
        refetch({
            statCurrency: convertCurrencyToData(selectedCurrency),
            filterCurrency: convertCurrencyToData(selectedCurrency),
            filterPeriod: convertFilterPeriod(period),
        });
    }, [period, refetch, selectedCurrency]);

    useEffect(() => {
        loadStats();
    }, [period, selectedCurrency, loadStats]);

    const handleChangePeriod = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setPeriod(convertValueToFilterPeriodValue(e.target.value));
    }

    const handleChangeCurrency = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setSelectedCurrency({currency: e.target.value});
    }

    return (
        <Box sx={{padding: isMobile ? 0 : "0 16px", maxWidth: "1200px", margin: "0 auto"}}>
            <Grid container
                  spacing={isMobile ? 0 : 3}
                  sx={{
                      margin: isMobile ? "22px auto" : "40px auto",
                      width: "100%",
                      padding: isMobile ? 0 : "0 16px",

                  }}>
                <Grid
                    item
                    id="stat"
                    xs={isMobile ? 12 : shouldRecise ? 5 : 3}
                    sx={{
                        padding: isMobile ? 0 : "0 16px",
                    }}
                >
                    <Typography
                        variant="h5"
                        component="h2"
                        sx={{
                            marginBottom: 2,
                        }}
                    >
                        Statistics
                    </Typography>
                    <Box sx={{
                        display: isMobile ? "flex" : "block",
                    }}>
                        <Box sx={{
                            display: "flex",
                            alignItems: "center",
                            width: isMobile ? 150 : 220,
                            height: isMobile ? 150 : 220,
                        }}>
                            <Diagram stat={{
                                total: data?.stat.total ?? 0,
                                currency: data?.stat.currency ?? "RUB",
                                statByCategories: data?.stat.statByCategories ?? [],
                            }}/>
                        </Box>
                        <Box id="legend-container" sx={{
                            display: "flex",
                            justifyContent: "center",
                            alignItems: "center"
                        }}>
                        </Box>
                    </Box>
                </Grid>
                <Grid
                    item
                    id="spendings"
                    xs={isMobile ? 12 : shouldRecise ? 7 : 9}
                    sx={{
                        padding: isMobile ? 0 : "0 16px",
                    }}
                >
                    <Typography
                        variant="h5"
                        component="h2"
                        sx={{
                            marginTop: isMobile ? 2 : 0,
                            marginBottom: 2,
                        }}
                    >
                        History of Spendings
                    </Typography>
                    <SpendingsTable
                        period={period}
                        handleChangePeriod={handleChangePeriod}
                        selectedCurrency={selectedCurrency}
                        handleChangeCurrency={handleChangeCurrency}
                        onDeleteCallback={loadStats}
                    />
                </Grid>
            </Grid>
        </Box>
    )
}
