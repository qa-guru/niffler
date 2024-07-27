import {Category} from "./Category.ts";

export interface Spending {
    id: string,
    amount: number,
    category: Category,
    currency: string,
    description: string,
    spendDate: string,
}