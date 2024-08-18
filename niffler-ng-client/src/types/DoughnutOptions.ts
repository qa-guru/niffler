export interface DoughnutOptions {
    cutout: string;
    responsive: boolean;
    maintainAspectRatio: boolean;
    layout: Record<string, any>,
    title: {
        display: boolean,
        text: string,
        currency: string,
    },
    plugins: {
        legend: {
            display: boolean,
        },
        htmlLegend: {
            containerID: string,
        },
    },
    tooltips: {
        enabled: boolean,
    },
    events: [],
}