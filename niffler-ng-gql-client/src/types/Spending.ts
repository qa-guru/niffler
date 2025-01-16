import {Spend} from "../generated/graphql.tsx";
import {Category} from "./Category.ts";

export interface Spending {
    id: string,
    amount: number,
    category: Category,
    currency: string,
    description: string,
    spendDate: string,
}

export type SpendingData = Omit<Spend, "category" | "id"> & {
    id?: string,
    category: {
        name: string,
    }
};
