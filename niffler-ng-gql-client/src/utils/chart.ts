import {Chart, Plugin} from "chart.js";
import {DoughnutOptions} from "../types/DoughnutOptions.ts";

const ENV = import.meta.env.MODE;
export const animationDuration = (ENV === "docker") ? 0 : 1500;

export const emptyCirclePlugin: Plugin = {
    id: 'emptyCirclePlugin',
    beforeDraw: (chart: Chart) => {
        const {ctx, chartArea: {left, right, top, bottom}, data} = chart;
        if (data.datasets && data.datasets[0] && data.datasets[0].data.length === 0) {
            const centerX = (left + right) / 2;
            const centerY = (top + bottom) / 2;
            const radius = Math.min(right - left, bottom - top) / 2 - 12;

            ctx.save();
            ctx.beginPath();
            ctx.arc(centerX, centerY, radius, 0, Math.PI * 2);
            ctx.strokeStyle = 'rgba(0, 0, 0, 0.1)';
            ctx.lineWidth = (chart.config.options as DoughnutOptions).arc.width;
            ctx.stroke();
            ctx.restore();
        }
    }
};

export const htmlLegendPlugin: Plugin = {
    id: 'htmlLegend',
    afterUpdate(chart: Chart) {
        const containerID = 'legend-container'
        const ul = getOrCreateLegendList(chart, containerID)
        const dataset = chart.data.datasets[0];

        while (ul.firstChild) {
            ul.firstChild.remove()
        }
        const generateLabels = chart.options.plugins?.legend?.labels?.generateLabels;
        const currency = (chart.config.options as DoughnutOptions).title.currency;
        const htmlLegend = (chart.config.options as DoughnutOptions).plugins.htmlLegend;
        if (generateLabels) {
            const items = generateLabels(chart);
            items.forEach((item: any, index: number) => {
                const datasetPoint = dataset.data[index];
                const selected = htmlLegend.selectedLabel === item.text;
                const li = document.createElement('li')
                li.style.alignItems = 'center'
                li.style.display = 'flex'
                li.style.flexDirection = 'row'

                // Text
                li.style.color = '#fff'
                li.style.margin = '4px 2px'
                li.style.padding = '4px 12px'
                li.style.display = 'block'
                li.style.backgroundColor = item.fillStyle;
                li.style.borderRadius = "20px"
                li.style.border = selected ? '1px solid #111827' : '1px solid transparent'
                li.style.boxSizing = 'border-box'
                li.style.cursor = 'pointer'
                li.style.fontWeight = selected ? '700' : '400'
                li.tabIndex = 0
                li.setAttribute('role', 'button')
                li.setAttribute('aria-pressed', String(selected))
                li.onclick = () => htmlLegend.onLabelClick(item.text)
                li.onkeydown = (event) => {
                    if (event.key === 'Enter' || event.key === ' ') {
                        event.preventDefault()
                        htmlLegend.onLabelClick(item.text)
                    }
                }

                const text = document.createTextNode(`${item.text} ${datasetPoint} ${currency}`)
                li.appendChild(text)

                ul.appendChild(li)
            })
        }
    },
}

const getOrCreateLegendList = (_chart: Chart, id: string) => {
    const legendContainer = document.getElementById(id) as HTMLElement
    let listContainer = legendContainer.querySelector('ul')

    if (!listContainer) {
        listContainer = document.createElement('ul')
        listContainer.style.display = 'flex'
        listContainer.style.justifyContent = 'center'
        listContainer.style.alignItems = "flex-start"
        listContainer.style.flexDirection = 'column'
        listContainer.style.margin = '10px'
        listContainer.style.padding = '0'

        legendContainer.appendChild(listContainer)
    }

    return listContainer
}

export const titlePlugin: Plugin = {
    id: 'titlePlugin',
    afterRender: (chart: Chart) => {
        const {ctx} = chart;
        if (!ctx) return;
        const xCoor = chart.chartArea.left + (chart.chartArea.right - chart.chartArea.left) / 2;
        const yCoor = chart.chartArea.top + (chart.chartArea.bottom - chart.chartArea.top) / 2;
        ctx.save();
        ctx.font = '1.2rem bold';
        ctx.fillStyle = 'black';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        const text = (chart.config.options as DoughnutOptions).title.text;

        ctx.fillText(`${text}`, xCoor, yCoor);

        ctx.restore();
    }
};
