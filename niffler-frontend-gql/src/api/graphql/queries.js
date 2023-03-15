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

export const QUERY_ALL_USERS = gql(`
    query Users {
        users {
            id
            username
            firstname
            surname
            photo
            friendState
        }
    }
`);

export const QUERY_CURRENT_USER = gql(`
    query CurrentUser {
        user {
            id
            username
            firstname
            surname
            currency
            photo
        }
    }
`);

export const QUERY_FRIENDS = gql(`
    query Friends {
        user {
            friends {
                id
                username
                firstname
                surname
                photo
                friendState
                friends {
                    id
                    username
                    friends {
                        id
                        username
                    }
                }
            }
            invitations {
                id
                username
                firstname
                surname
                photo
                friendState
            }
        } 
    }
`);

export const QUERY_SPENDS = gql(`
    query Spends($filterPeriod: FilterPeriod, $filterCurrency: CurrencyValues) {
            spends(filterPeriod: $filterPeriod, filterCurrency: $filterCurrency) {
               id
               spendDate
               category
               currency
               amount
               description
            }
        }
`);

export const QUERY_INVITATIONS = gql(`
query Invitations {
        user {
            invitations {
                id
                username
                firstname
                surname
                photo
            }
        } 
    }
`);
