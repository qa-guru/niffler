import {BarElement, CategoryScale, Chart as ChartJS, Legend, LinearScale, Title, Tooltip} from "chart.js";
import {useContext, useEffect, useState} from "react";
import {Bar} from "react-chartjs-2";
import {CurrencyContext} from "../../contexts/CurrencyContext";

export const SpendingStatistics = ({ statistic, defaultCurrency}) => {
    const {selectedCurrency} = useContext(CurrencyContext);


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
                text: "Statistics",
                font: {size: "24px"},
                color: "black",
            },
        },
    };

    const getLabels = () => {
        if (selectedCurrency.value !== "ALL") {
            return statistic.find(v => (v.currency === selectedCurrency.value))?.categoryStatistics?.map(v => v.category);
        }
        return statistic.find(v => (v.currency === defaultCurrency))?.categoryStatistics?.map(v => v.category);
    }


    const getData = () => {
        const allCategories = getLabels();
        const map = new Map();
            if(allCategories !== undefined) {
                for(let c of allCategories) {
                    let total;
                    if (selectedCurrency.value !== "ALL") {
                        total = statistic
                            .filter(st => st.currency === selectedCurrency.value)
                            .flatMap(st => st?.categoryStatistics)
                            .find(cat => cat?.category === c)?.total;
                    } else {
                        total = statistic
                            .flatMap(st => st?.categoryStatistics)
                            .filter(cat => cat?.category === c)
                            .reduce((sum, cat) => sum + cat.totalInUserDefaultCurrency, 0);
                    }
                    map.set(c, total);
                }
            }
        return map;
    };
    const data = getData();

    const [chartData, setChartData] = useState({
        labels: Array.from(data.keys()),
        datasets:[{
            data: Array.from(data.values()),
            maxBarThickness: 70,
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
        const newData = getData();
        setChartData({
            labels: Array.from(newData.keys()),
            datasets:[{
                data: Array.from(newData.values()),
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

    }, [statistic, selectedCurrency]);

    return (
        <section className={"main-content__section main-content__section-stats"}>
                <Bar options={options} data={chartData} />
        </section>
)
}
