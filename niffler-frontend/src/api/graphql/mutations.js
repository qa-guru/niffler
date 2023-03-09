import {gql} from "@apollo/client";

export const CREATE_CATEGORY_MUTATION = gql(`
    mutation CreateCategory($input: CreateCategoryInput!) {
        createCategory(input: $input) {
            id
            category
        }
    }
`);
