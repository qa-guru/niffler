import { Box, Grid, Typography, useMediaQuery, useTheme } from "@mui/material";
import { SpendingsTable } from "../../components/SpendingsTable";
import { ChangeEvent, useEffect, useRef, useState } from "react";
import { apiClient } from "../../api/apiClient.ts";
import { Statistic } from "../../types/Statistic.ts";
import { STAT_INITIAL_STATE } from "../../context/SessionContext.tsx";
import { convertValueToFilterPeriodValue, FilterPeriodValue } from "../../types/FilterPeriod.ts";
import { convertFilterPeriod } from "../../utils/dataConverter.ts";
import { convertCurrencyToData, Currency } from "../../types/Currency.ts";
import { Diagram } from "../../components/Diagram";
import { useSnackBar } from "../../context/SnackBarContext.tsx";
import { ensureServiceWorkerReady, requestNotificationPermission, subscribeForegroundMessages, syncFcmTokenIfChanged } from '../../firebase';

export const MainPage = () => {
    const [period, setPeriod] = useState<FilterPeriodValue>({ label: "All time", value: "ALL" });
    const [stat, setStatistic] = useState<Statistic>(STAT_INITIAL_STATE);
    const [selectedCurrency, setSelectedCurrency] = useState<Currency>(
        { currency: "ALL" });
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const shouldRecise = useMediaQuery(theme.breakpoints.down('md'));
    const snackbar = useSnackBar();

    const loadStats = () => {
        const currency = convertCurrencyToData(selectedCurrency);
        apiClient.getStat({
            onSuccess: (data) => {
                setStatistic(data);
            },
            onFailure: (e) => {
                console.error(e.message);
                snackbar.showSnackBar(e.message, "error");
            },
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
        setSelectedCurrency({ currency: e.target.value });
    }

    const pushInitOnce = useRef(false);

    useEffect(() => {
        if (pushInitOnce.current) return;
        pushInitOnce.current = true;

        if (!(location.protocol === 'https:' || location.hostname === 'localhost')) {
            console.warn('Push init skipped: not HTTPS');
            return;
        }
        if (!('serviceWorker' in navigator) || !('Notification' in window)) {
            console.warn('Push init skipped: SW or Notification unsupported');
            return;
        }

        let cleanups: Array<() => void> = [];

        (async () => {
            try {
                const swReg = await ensureServiceWorkerReady();
                const perm = await requestNotificationPermission();
                if (perm !== 'granted') {
                    if (perm === 'denied') {
                        snackbar.showSnackBar("Notifications are disabled in your browser", "info");
                    }
                    return;
                }

                const sync = async () => {
                    await syncFcmTokenIfChanged(swReg, async (token) => {
                        await apiClient.registerFirebaseToken(
                            { token, userAgent: navigator.userAgent },
                            {
                                onSuccess: () => {
                                    snackbar.showSnackBar("Push token registered", "success");
                                },
                                onFailure: (e) => {
                                    console.error(e.message);
                                    snackbar.showSnackBar("Push token registration error", "error");
                                },
                            }
                        );
                    });
                };

                await sync();

                const onControllerChange = () => { void sync(); };
                navigator.serviceWorker.addEventListener('controllerchange', onControllerChange);
                cleanups.push(() => navigator.serviceWorker.removeEventListener('controllerchange', onControllerChange));
                const onVis = () => { if (document.visibilityState === 'visible') void sync(); };
                document.addEventListener('visibilitychange', onVis);
                cleanups.push(() => document.removeEventListener('visibilitychange', onVis));

                const unsubFgPromise = subscribeForegroundMessages((payload) => {
                    console.debug('Foreground push:', payload);
                });
                unsubFgPromise.then((unsub) => cleanups.push(() => { try { unsub(); } catch { } }));

            } catch (e: any) {
                console.error(e);
                snackbar.showSnackBar(e?.message || "Error while initializing push messages", "error");
            }
        })();

        return () => { cleanups.forEach(fn => { try { fn(); } catch { } }); };
    }, [snackbar]);

    return (
        <Box sx={{ padding: isMobile ? 0 : "0 16px", maxWidth: "1200px", margin: "0 auto" }}>
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
                        <Box id={"chart"}
                            sx={{
                                display: "flex",
                                alignItems: "center",
                                width: isMobile ? 150 : 220,
                                height: isMobile ? 150 : 220,
                            }}>
                            <Diagram stat={stat} />
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
