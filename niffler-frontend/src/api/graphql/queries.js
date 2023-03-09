import {gql} from "@apollo/client";

export const QUERY_ALL_CATEGORIES = gql(`
    query GetAllCategories {
        categories {
            id
            category
        }
    }
`);

export const QUERY_ALL_CURRENCIES = gql(`
    query GetAllCurrencies {
        currencies {
            currency
            currencyRate
        }
    }
`);
