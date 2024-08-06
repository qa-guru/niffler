import {Chart} from "chart.js";

export const htmlLegendPlugin = {
    id: 'htmlLegend',
    afterUpdate(chart: Chart, args: any, options: any) {
        const containerID = 'legend-container'
        const ul = getOrCreateLegendList(chart, containerID)
        const dataset = chart.data.datasets[0];

        // Remove old legend items
        while (ul.firstChild) {
            ul.firstChild.remove()
        }

        // Reuse the built-in legendItems generator
        const items = chart.options.plugins.legend.labels.generateLabels(chart)

        items.forEach((item: any, index: number) => {
            const datasetPoint = dataset.data[index];
            const li = document.createElement('li')
            li.style.alignItems = 'center'
            li.style.display = 'flex'
            li.style.flexDirection = 'row'

            // Text
            li.style.color = '#fff'
            li.style.margin = '2px'
            li.style.padding = '4px 12px'
            li.style.display = 'block'
            li.style.backgroundColor = item.fillStyle;
            li.style.borderRadius = "20px"

            const text = document.createTextNode(`${item.text} ${datasetPoint}`)
            li.appendChild(text)

            ul.appendChild(li)
        })
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

export const titlePlugin = (text: string) => ({
    id: 'titlePlugin',
    beforeDraw: (chart: Chart) => {
        const ctx = chart.ctx;
        const xCoor = chart.chartArea.left + (chart.chartArea.right - chart.chartArea.left) / 2;
        const yCoor = chart.chartArea.top + (chart.chartArea.bottom - chart.chartArea.top) / 2;
        ctx.save();
        ctx.font = '1.2rem bold';
        ctx.fillStyle = 'black';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText(`${text}`, xCoor, yCoor);
        ctx.restore();
    }
});
