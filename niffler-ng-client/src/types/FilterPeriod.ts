export const filterPeriod = [
    {
        value: "ALL",
        label: "All time",
    },
    {
        value: "MONTH",
        label: "Last month",
    },
    {
        value: "WEEK",
        label: "Last week",
    },
    {
        value: "TODAY",
        label: "Today",
    },
] as const;

export type FilterPeriodValue = typeof filterPeriod[number];

export const convertValueToFilterPeriodValue = (value: string): FilterPeriodValue => {
    const res = filterPeriod.find(period => period.value === value);
    if (res) {
        return res
    } else {
        throw Error("Bad period value");
    }
}