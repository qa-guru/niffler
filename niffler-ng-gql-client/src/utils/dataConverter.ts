import {FilterPeriodValue} from "../types/FilterPeriod.ts";
import {FilterPeriod} from "../generated/graphql.tsx";

export const convertFilterPeriod = (period: FilterPeriodValue) => {
    switch (period.value) {
        case "TODAY":
            return FilterPeriod.Today
        case "WEEK":
            return FilterPeriod.Week
        case "MONTH":
            return FilterPeriod.Month;
        default:
            return;
    }
}

