import {CategoryStatistic} from "./CategoryStatistic.ts";

export interface Statistic {
    total: number,
    currency: string,
    statByCategories: CategoryStatistic[],
}
