import {CategoryStatistic} from "./CategoryStatistic.ts";

export interface Statistic {
    total: number,
    statByCategories: CategoryStatistic[],
}