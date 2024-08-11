import {Box, Container, Grid, Typography} from "@mui/material";
import {SpendingsTable} from "../../components/SpendingsTable";
import {ChangeEvent, useEffect, useState} from "react";
import {apiClient} from "../../api/apiClient.ts";
import {Statistic} from "../../types/Statistic.ts";
import {STAT_INITIAL_STATE} from "../../context/SessionContext.tsx";
import {convertValueToFilterPeriodValue, FilterPeriodValue} from "../../types/FilterPeriod.ts";
import {convertFilterPeriod} from "../../utils/dataConverter.ts";
import {convertCurrencyToData, Currency} from "../../types/Currency.ts";
import {Diagram} from "../../components/Diagram";

export const MainPage = () => {
    const [period, setPeriod] = useState<FilterPeriodValue>({label: "All time", value: "ALL"});
    const [stat, setStatistic] = useState<Statistic>(STAT_INITIAL_STATE);
    const [selectedCurrency, setSelectedCurrency] = useState<Currency>(
        {currency: "ALL"});

    const loadStats = () => {
        const currency = convertCurrencyToData(selectedCurrency);
        apiClient.getStat({
                onSuccess: (data) => {
                    setStatistic(data);
                },
                onFailure: (e) => console.error(e.message),
            },
            convertFilterPeriod(period),
            currency,
        );
    }

    useEffect(() => {
        loadStats();
    }, [period, selectedCurrency]);

    const handleChangePeriod = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setPeriod(convertValueToFilterPeriodValue(e.target.value));
    }

    const handleChangeCurrency = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setSelectedCurrency({currency: e.target.value});
    }

    return (
        <Container>
            <Grid container
                  spacing={2}
                  sx={{
                      margin: "40px auto",
                      width: "100%",
                  }}>
                <Grid item xs={3}>
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
                        display: "flex",
                        alignItems: "center",
                        width: 220,
                        height: 220,
                    }}>
                        <Diagram stat={stat}/>
                    </Box>
                    <Box id="legend-container" sx={{
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center"
                    }}></Box>
                </Grid>
                <Grid item xs={9}>
                    <Typography
                        variant="h5"
                        component="h2"
                        sx={{
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
        </Container>
    )
}
