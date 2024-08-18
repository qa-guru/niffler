import {FilterPeriodValue} from "../types/FilterPeriod.ts";

export const convertFilterPeriod = (period: FilterPeriodValue) => {
    switch (period.value) {
        case "TODAY":
        case "WEEK":
        case "MONTH":
            return period.value;
        default:
            return "";
    }
}

