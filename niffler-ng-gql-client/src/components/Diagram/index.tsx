import {Doughnut} from "react-chartjs-2";
import {FC} from "react";
import {useMediaQuery, useTheme} from "@mui/material";
import {Statistic} from "../../types/Statistic.ts";
import {DoughnutOptions} from "../../types/DoughnutOptions.ts";
import {ArcElement, Chart, Legend} from "chart.js";
import {emptyCirclePlugin, htmlLegendPlugin, titlePlugin} from "../../utils/chart.ts";
import {CurrencyValue, getCurrencyIcon} from "../../types/Currency.ts";

interface DiagramInterface {
    stat: Statistic,
}

Chart.register(ArcElement, Legend);
Chart.register(htmlLegendPlugin);
Chart.register(titlePlugin);
Chart.register(emptyCirclePlugin);

export const Diagram: FC<DiagramInterface> = ({stat}) => {

    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));


    const data = {
        title: stat.total,
        labels: stat.statByCategories.map((s) => s.categoryName),
        datasets: [
            {
                label: "stats",
                data: stat.statByCategories.map((s) => s.sum),
                backgroundColor: [
                    theme.palette.yellow.main,
                    theme.palette.green.main,
                    theme.palette.orange.main,
                    theme.palette.blue100.main,
                    theme.palette.azure.main,
                    theme.palette.blue200.main,
                    theme.palette.red.main,
                    theme.palette.skyBlue.main,
                    theme.palette.purple.main,
                ],
            }
        ],
    };

    const options: DoughnutOptions = {
        cutout: "75%",
        responsive: true,
        events: [],
        maintainAspectRatio: false,
        layout: {
            padding: 0
        },
        title: {
            display: true,
            text: `${stat.total} ${getCurrencyIcon(stat.currency as CurrencyValue)}`,
            currency: getCurrencyIcon(stat.currency as CurrencyValue),
        },
        arc: {
            width: isMobile ? 16 : 23,
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
        <Doughnut data={data} options={options}/>
    )
}