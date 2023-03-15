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

export const ADD_FRIEND_MUTATION = gql(`
    mutation AddFriend($username: String!){
        addFriend(friendUsername: $username) {
            id
            username
            friendState
        }
    }
`);

export const ACCEPT_INVITATION_MUTATION = gql(`
    mutation AcceptInvitation($username: String!){
        acceptInvitation(friendUsername: $username) {
            id
            username
            friendState
        }
    }
`);

export const DECLINE_INVITATION_MUTATION = gql(`
    mutation DeclineInvitation($username: String!){
        declineInvitation(friendUsername: $username) {
            id
            username
            friendState}
    }
`);

export const REMOVE_FRIEND_MUTATION = gql(`
    mutation RemoveFriend($username: String!){
        removeFriend(friendUsername: $username) {
            id
            username
            friendState
        }
    }
`);
