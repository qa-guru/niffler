import {Box, Container, Grid, Typography, useTheme} from "@mui/material";
import {ArcElement, Chart, Legend } from 'chart.js';
import {Doughnut} from "react-chartjs-2";
import {htmlLegendPlugin, titlePlugin} from "../../utils/chart.ts";
import {SpendingsTable} from "../../components/SpendingsTable";
import {useEffect, useState} from "react";
import {apiClient} from "../../api/apiClient.ts";
import {Statistic} from "../../types/Statistic.ts";
import {STAT_INITIAL_STATE} from "../../context/SessionContext.tsx";

Chart.register(ArcElement, Legend);

export const StatisticsPage = () => {
    const [stat, setStatistic] = useState<Statistic>(STAT_INITIAL_STATE);

    useEffect(() => {
        apiClient.getStat({
            onSuccess: (data) => {
                console.log(data);
                setStatistic(data);
            },
            onFailure: (e) => console.log(e),
        });
    }, []);

    const theme = useTheme();
    // const [page, setPage] = useState(0);

    const data = {
        title: stat.total,
        legend: "100000р",
        labels: stat.statByCategories.map((s) => s.categoryName),
        // labels: ["Foo", "Bar"],
        datasets: [
            {
                label: "stats",
                data: stat.statByCategories.map((s) => s.sum),
                // data: ["100", "200"],
                backgroundColor: [
                    theme.palette.blue100.main,
                    theme.palette.red.main,
                    theme.palette.azure.main,
                    theme.palette.blue200.main,
                    theme.palette.orange.main,
                    theme.palette.skyBlue.main,
                    theme.palette.yellow.main,
                    theme.palette.purple.main,
                    theme.palette.green.main
                ],
            }
        ],
    }

    const options = {
        cutout: "75%",
        responsive: true,
        maintainAspectRatio: false,
        layout: {
            padding: 0
        },
        title: {
            display: true,
            text: "Title"
        },
        plugins: {
            legend: {
                display: false,
            },
            htmlLegend: {
                containerID: 'legend-container',
            },
        },
        tooltips: {
            enabled: false,
        },
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
                        <Doughnut data={data} options={options} plugins={[htmlLegendPlugin, titlePlugin(data.title)]}/>
                        {/*<Doughnut data={data} options={options} plugins={[htmlLegendPlugin, titlePlugin("300")]}/>*/}
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
                    <SpendingsTable/>
                </Grid>

            </Grid>
        </Container>
    )
}

