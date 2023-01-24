import {BarElement, CategoryScale, Chart as ChartJS, Legend, LinearScale, Title, Tooltip} from "chart.js";
import {useEffect, useState} from "react";
import {Bar} from "react-chartjs-2";

export const SpendingStatistics = ({currency, statistic}) => {

    ChartJS.register(
        CategoryScale,
        LinearScale,
        BarElement,
        Title,
        Tooltip,
        Legend
    );

    const options = {
        responsive: true,
        plugins: {
            legend: {
                display: false,
            },
            title: {
                display: true,
                text: 'Statistics',
            },
        },
    };


    const [chartData, setChartData] = useState({
        labels: statistic.find(v => (v.currency === currency))?.categoryStatistics?.map(v => v.category),
        datasets:[{
            data: statistic.find(v => (v.currency === currency))?.categoryStatistics?.map(v => v.total),
            backgroundColor: [
                "rgba(90, 34, 139, 0.5)",
                "rgba(213, 184, 255, 0.5)",
                "rgba(165, 55, 253, 0.5)",
                "rgba(102, 51, 153, 0.5)",
                "rgba(241, 231, 254, 0.5)",
                "rgba(140, 20, 252, 0.5)",
            ],
            border: true
        }]
    });

    useEffect(() => {
        setChartData({
            labels:statistic.find(v => (v.currency === currency))?.categoryStatistics?.map(v => v.category),
            datasets:[{
                data: statistic.find(v => (v.currency === currency))?.categoryStatistics?.map(v => v.total),
                backgroundColor: [
                    "rgba(90, 34, 139, 0.5)",
                    "rgba(213, 184, 255, 0.5)",
                    "rgba(165, 55, 253, 0.5)",
                    "rgba(102, 51, 153, 0.5)",
                    "rgba(241, 231, 254, 0.5)",
                    "rgba(140, 20, 252, 0.5)",
                ],
                border: true
            }]
        })

    }, [statistic]);

    return (
        <section className={"main-content__section main-content__section-stats"}>
            <Bar options={options} data={chartData} />
        </section>
)
}
