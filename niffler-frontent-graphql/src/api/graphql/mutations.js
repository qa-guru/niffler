import {gql} from "@apollo/client";

export const CREATE_CATEGORY_MUTATION = gql(`
    mutation CreateCategory($input: CreateCategoryInput!) {
        createCategory(input: $input) {
            id
            category
        }
    }
`);

export const UPDATE_USER_INFO_MUTATION = gql(`
    mutation UpdateUser($input: UpdateUserInfoInput!) {
        updateUser(input: $input) {
            id
            username
            firstname
            surname
            photo
            currency
        }
    }
`);

export const ADD_SPEND_MUTATION = gql(`
      mutation AddSpend($spend: SpendInput!) {
        addSpend(spend: $spend) {
               id
               spendDate
               category
               currency
               amount
               description
        }
    }
`);

export const UPDATE_SPEND_MUTATION = gql(`
    mutation UpdateSpend($spend: UpdateSpendInput!) {
            updateSpend(spend: $spend) {
                   id
                   spendDate
                   category
                   currency
                   amount
                   description
            }
        }
`);

export const DELETE_SPENDS_MUTATION = gql(`
     mutation DeleteSpends($ids: [String]!) {
            deleteSpends(ids: $ids) 
        }
`);
