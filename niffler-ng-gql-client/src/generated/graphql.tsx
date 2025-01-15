import * as Apollo from '@apollo/client';
import {gql} from '@apollo/client';

export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type MakeEmpty<T extends { [key: string]: unknown }, K extends keyof T> = { [_ in K]?: never };
export type Incremental<T> = T | { [P in keyof T]?: P extends ' $fragmentName' | '__typename' ? T[P] : never };
const defaultOptions = {} as const;
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
    ID: { input: string; output: string; }
    String: { input: string; output: string; }
    Boolean: { input: boolean; output: boolean; }
    Int: { input: number; output: number; }
    Float: { input: number; output: number; }
    Date: { input: any; output: any; }
};

export type Category = {
    __typename?: 'Category';
    archived: Scalars['Boolean']['output'];
    id: Scalars['ID']['output'];
    name: Scalars['String']['output'];
    username: Scalars['String']['output'];
};

export type CategoryInput = {
    archived: Scalars['Boolean']['input'];
    id?: InputMaybe<Scalars['ID']['input']>;
    name: Scalars['String']['input'];
};

export type Currency = {
    __typename?: 'Currency';
    currency: CurrencyValues;
    currencyRate: Scalars['Float']['output'];
};

export enum CurrencyValues {
    Eur = 'EUR',
    Kzt = 'KZT',
    Rub = 'RUB',
    Usd = 'USD'
}

export enum FilterPeriod {
    Month = 'MONTH',
    Today = 'TODAY',
    Week = 'WEEK'
}

export enum FriendshipAction {
    Accept = 'ACCEPT',
    Add = 'ADD',
    Delete = 'DELETE',
    Reject = 'REJECT'
}

export type FriendshipInput = {
    action: FriendshipAction;
    username: Scalars['String']['input'];
};

export enum FriendshipStatus {
    Friend = 'FRIEND',
    InviteReceived = 'INVITE_RECEIVED',
    InviteSent = 'INVITE_SENT'
}

export type Mutation = {
    __typename?: 'Mutation';
    category: Category;
    deleteSpend: Array<Scalars['ID']['output']>;
    friendship: User;
    spend: Spend;
    user: User;
};


export type MutationCategoryArgs = {
    input: CategoryInput;
};


export type MutationDeleteSpendArgs = {
    ids: Array<Scalars['ID']['input']>;
};


export type MutationFriendshipArgs = {
    input: FriendshipInput;
};


export type MutationSpendArgs = {
    input: SpendInput;
};


export type MutationUserArgs = {
    input: UserInput;
};

export type PageInfo = {
    __typename?: 'PageInfo';
    endCursor?: Maybe<Scalars['String']['output']>;
    hasNextPage: Scalars['Boolean']['output'];
    hasPreviousPage: Scalars['Boolean']['output'];
    startCursor?: Maybe<Scalars['String']['output']>;
};

export type Query = {
    __typename?: 'Query';
    allPeople?: Maybe<UserConnection>;
    currencies: Array<Currency>;
    session: Session;
    spend: Spend;
    spends?: Maybe<SpendConnection>;
    stat: Statistic;
    user: User;
};


export type QueryAllPeopleArgs = {
    page: Scalars['Int']['input'];
    searchQuery?: InputMaybe<Scalars['String']['input']>;
    size: Scalars['Int']['input'];
    sort?: InputMaybe<Array<Scalars['String']['input']>>;
};


export type QuerySpendArgs = {
    id: Scalars['ID']['input'];
};


export type QuerySpendsArgs = {
    filterCurrency?: InputMaybe<CurrencyValues>;
    filterPeriod?: InputMaybe<FilterPeriod>;
    page: Scalars['Int']['input'];
    searchQuery?: InputMaybe<Scalars['String']['input']>;
    size: Scalars['Int']['input'];
    sort?: InputMaybe<Array<Scalars['String']['input']>>;
};


export type QueryStatArgs = {
    filterCurrency?: InputMaybe<CurrencyValues>;
    filterPeriod?: InputMaybe<FilterPeriod>;
    statCurrency?: InputMaybe<CurrencyValues>;
};

export type Session = {
    __typename?: 'Session';
    expiresAt?: Maybe<Scalars['Date']['output']>;
    issuedAt?: Maybe<Scalars['Date']['output']>;
    username?: Maybe<Scalars['String']['output']>;
};

export type Spend = {
    __typename?: 'Spend';
    amount: Scalars['Float']['output'];
    category: Category;
    currency: CurrencyValues;
    description?: Maybe<Scalars['String']['output']>;
    id: Scalars['ID']['output'];
    spendDate: Scalars['Date']['output'];
    username?: Maybe<Scalars['String']['output']>;
};

export type SpendConnection = {
    __typename?: 'SpendConnection';
    edges: Array<Maybe<SpendEdge>>;
    pageInfo: PageInfo;
};

export type SpendEdge = {
    __typename?: 'SpendEdge';
    cursor: Scalars['String']['output'];
    node: Spend;
};

export type SpendInput = {
    amount: Scalars['Float']['input'];
    category: CategoryInput;
    currency: CurrencyValues;
    description?: InputMaybe<Scalars['String']['input']>;
    id?: InputMaybe<Scalars['ID']['input']>;
    spendDate: Scalars['Date']['input'];
};

export type Statistic = {
    __typename?: 'Statistic';
    currency: CurrencyValues;
    statByCategories: Array<SumByCategory>;
    total: Scalars['Float']['output'];
};

export type SumByCategory = {
    __typename?: 'SumByCategory';
    categoryName: Scalars['String']['output'];
    currency: CurrencyValues;
    firstSpendDate: Scalars['Date']['output'];
    lastSpendDate: Scalars['Date']['output'];
    sum: Scalars['Float']['output'];
};

export type User = {
    __typename?: 'User';
    categories: Array<Category>;
    friends?: Maybe<UserConnection>;
    friendshipStatus?: Maybe<FriendshipStatus>;
    fullname?: Maybe<Scalars['String']['output']>;
    id: Scalars['ID']['output'];
    photo?: Maybe<Scalars['String']['output']>;
    photoSmall?: Maybe<Scalars['String']['output']>;
    username: Scalars['String']['output'];
};


export type UserFriendsArgs = {
    page: Scalars['Int']['input'];
    searchQuery?: InputMaybe<Scalars['String']['input']>;
    size: Scalars['Int']['input'];
    sort?: InputMaybe<Array<Scalars['String']['input']>>;
};

export type UserConnection = {
    __typename?: 'UserConnection';
    edges: Array<Maybe<UserEdge>>;
    pageInfo: PageInfo;
};

export type UserEdge = {
    __typename?: 'UserEdge';
    cursor: Scalars['String']['output'];
    node: User;
};

export type UserInput = {
    fullname?: InputMaybe<Scalars['String']['input']>;
    photo?: InputMaybe<Scalars['String']['input']>;
};

export type UpdateCategoryMutationVariables = Exact<{
    input: CategoryInput;
}>;


export type UpdateCategoryMutation = {
    __typename?: 'Mutation',
    category: { __typename?: 'Category', id: string, name: string, archived: boolean }
};

export type UpdateSpendMutationVariables = Exact<{
    input: SpendInput;
}>;


export type UpdateSpendMutation = {
    __typename?: 'Mutation',
    spend: {
        __typename?: 'Spend',
        id: string,
        spendDate: any,
        currency: CurrencyValues,
        amount: number,
        description?: string | null,
        category: { __typename?: 'Category', id: string, name: string, archived: boolean }
    }
};

export type DeleteSpendsMutationVariables = Exact<{
    ids: Array<Scalars['ID']['input']> | Scalars['ID']['input'];
}>;


export type DeleteSpendsMutation = { __typename?: 'Mutation', deleteSpend: Array<string> };

export type UpdateUserMutationVariables = Exact<{
    input: UserInput;
}>;


export type UpdateUserMutation = {
    __typename?: 'Mutation',
    user: { __typename?: 'User', id: string, username: string, fullname?: string | null, photo?: string | null }
};

export type UpdateFriendshipStatusMutationVariables = Exact<{
    input: FriendshipInput;
}>;


export type UpdateFriendshipStatusMutation = {
    __typename?: 'Mutation',
    friendship: { __typename?: 'User', id: string, username: string, fullname?: string | null, photo?: string | null }
};

export type CurrenciesQueryVariables = Exact<{ [key: string]: never; }>;


export type CurrenciesQuery = {
    __typename?: 'Query',
    currencies: Array<{ __typename?: 'Currency', currency: CurrencyValues }>
};

export type SessionQueryVariables = Exact<{ [key: string]: never; }>;


export type SessionQuery = { __typename?: 'Query', session: { __typename?: 'Session', username?: string | null } };

export type SpendsQueryVariables = Exact<{
    page: Scalars['Int']['input'];
    size: Scalars['Int']['input'];
    sort?: InputMaybe<Array<Scalars['String']['input']> | Scalars['String']['input']>;
    searchQuery?: InputMaybe<Scalars['String']['input']>;
    filterPeriod?: InputMaybe<FilterPeriod>;
    filterCurrency?: InputMaybe<CurrencyValues>;
}>;


export type SpendsQuery = {
    __typename?: 'Query',
    spends?: {
        __typename?: 'SpendConnection',
        edges: Array<{
            __typename?: 'SpendEdge',
            node: {
                __typename?: 'Spend',
                id: string,
                spendDate: any,
                currency: CurrencyValues,
                amount: number,
                description?: string | null,
                category: { __typename?: 'Category', id: string, name: string }
            }
        } | null>,
        pageInfo: { __typename?: 'PageInfo', hasPreviousPage: boolean, hasNextPage: boolean }
    } | null
};

export type SpendQueryVariables = Exact<{
    id: Scalars['ID']['input'];
}>;


export type SpendQuery = {
    __typename?: 'Query',
    spend: {
        __typename?: 'Spend',
        id: string,
        spendDate: any,
        currency: CurrencyValues,
        amount: number,
        description?: string | null,
        category: { __typename?: 'Category', name: string }
    }
};

export type StatQueryVariables = Exact<{
    filterPeriod?: InputMaybe<FilterPeriod>;
    filterCurrency?: InputMaybe<CurrencyValues>;
    statCurrency?: InputMaybe<CurrencyValues>;
}>;


export type StatQuery = {
    __typename?: 'Query',
    stat: {
        __typename?: 'Statistic',
        total: number,
        currency: CurrencyValues,
        statByCategories: Array<{
            __typename?: 'SumByCategory',
            categoryName: string,
            currency: CurrencyValues,
            sum: number,
            firstSpendDate: any,
            lastSpendDate: any
        }>
    }
};

export type CurrentUserQueryVariables = Exact<{ [key: string]: never; }>;


export type CurrentUserQuery = {
    __typename?: 'Query',
    user: {
        __typename?: 'User',
        id: string,
        username: string,
        fullname?: string | null,
        photo?: string | null,
        categories: Array<{ __typename?: 'Category', id: string, name: string, archived: boolean }>
    }
};

export type CategoriesQueryVariables = Exact<{ [key: string]: never; }>;


export type CategoriesQuery = {
    __typename?: 'Query',
    user: {
        __typename?: 'User',
        categories: Array<{ __typename?: 'Category', id: string, name: string, archived: boolean }>
    }
};

export type FriendsQueryVariables = Exact<{
    page: Scalars['Int']['input'];
    size: Scalars['Int']['input'];
    sort?: InputMaybe<Array<Scalars['String']['input']> | Scalars['String']['input']>;
    searchQuery?: InputMaybe<Scalars['String']['input']>;
}>;


export type FriendsQuery = {
    __typename?: 'Query',
    user: {
        __typename?: 'User',
        friends?: {
            __typename?: 'UserConnection',
            edges: Array<{
                __typename?: 'UserEdge',
                node: {
                    __typename?: 'User',
                    id: string,
                    username: string,
                    photoSmall?: string | null,
                    fullname?: string | null,
                    friendshipStatus?: FriendshipStatus | null
                }
            } | null>,
            pageInfo: { __typename?: 'PageInfo', hasPreviousPage: boolean, hasNextPage: boolean }
        } | null
    }
};

export type AllPeopleQueryVariables = Exact<{
    page: Scalars['Int']['input'];
    size: Scalars['Int']['input'];
    sort?: InputMaybe<Array<Scalars['String']['input']> | Scalars['String']['input']>;
    searchQuery?: InputMaybe<Scalars['String']['input']>;
}>;


export type AllPeopleQuery = {
    __typename?: 'Query',
    allPeople?: {
        __typename?: 'UserConnection',
        edges: Array<{
            __typename?: 'UserEdge',
            node: {
                __typename?: 'User',
                id: string,
                username: string,
                photoSmall?: string | null,
                fullname?: string | null,
                friendshipStatus?: FriendshipStatus | null
            }
        } | null>,
        pageInfo: { __typename?: 'PageInfo', hasPreviousPage: boolean, hasNextPage: boolean }
    } | null
};


export const UpdateCategoryDocument = gql`
    mutation UpdateCategory($input: CategoryInput!) {
        category(input: $input) {
            id
            name
            archived
        }
    }
`;
export type UpdateCategoryMutationFn = Apollo.MutationFunction<UpdateCategoryMutation, UpdateCategoryMutationVariables>;

/**
 * __useUpdateCategoryMutation__
 *
 * To run a mutation, you first call `useUpdateCategoryMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateCategoryMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateCategoryMutation, { data, loading, error }] = useUpdateCategoryMutation({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useUpdateCategoryMutation(baseOptions?: Apollo.MutationHookOptions<UpdateCategoryMutation, UpdateCategoryMutationVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useMutation<UpdateCategoryMutation, UpdateCategoryMutationVariables>(UpdateCategoryDocument, options);
}

export type UpdateCategoryMutationHookResult = ReturnType<typeof useUpdateCategoryMutation>;
export type UpdateCategoryMutationResult = Apollo.MutationResult<UpdateCategoryMutation>;
export type UpdateCategoryMutationOptions = Apollo.BaseMutationOptions<UpdateCategoryMutation, UpdateCategoryMutationVariables>;
export const UpdateSpendDocument = gql`
    mutation UpdateSpend($input: SpendInput!) {
        spend(input: $input) {
            id
            spendDate
            category {
                id
                name
                archived
            }
            currency
            amount
            description
        }
    }
`;
export type UpdateSpendMutationFn = Apollo.MutationFunction<UpdateSpendMutation, UpdateSpendMutationVariables>;

/**
 * __useUpdateSpendMutation__
 *
 * To run a mutation, you first call `useUpdateSpendMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateSpendMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateSpendMutation, { data, loading, error }] = useUpdateSpendMutation({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useUpdateSpendMutation(baseOptions?: Apollo.MutationHookOptions<UpdateSpendMutation, UpdateSpendMutationVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useMutation<UpdateSpendMutation, UpdateSpendMutationVariables>(UpdateSpendDocument, options);
}

export type UpdateSpendMutationHookResult = ReturnType<typeof useUpdateSpendMutation>;
export type UpdateSpendMutationResult = Apollo.MutationResult<UpdateSpendMutation>;
export type UpdateSpendMutationOptions = Apollo.BaseMutationOptions<UpdateSpendMutation, UpdateSpendMutationVariables>;
export const DeleteSpendsDocument = gql`
    mutation DeleteSpends($ids: [ID!]!) {
        deleteSpend(ids: $ids)
    }
`;
export type DeleteSpendsMutationFn = Apollo.MutationFunction<DeleteSpendsMutation, DeleteSpendsMutationVariables>;

/**
 * __useDeleteSpendsMutation__
 *
 * To run a mutation, you first call `useDeleteSpendsMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useDeleteSpendsMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [deleteSpendsMutation, { data, loading, error }] = useDeleteSpendsMutation({
 *   variables: {
 *      ids: // value for 'ids'
 *   },
 * });
 */
export function useDeleteSpendsMutation(baseOptions?: Apollo.MutationHookOptions<DeleteSpendsMutation, DeleteSpendsMutationVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useMutation<DeleteSpendsMutation, DeleteSpendsMutationVariables>(DeleteSpendsDocument, options);
}

export type DeleteSpendsMutationHookResult = ReturnType<typeof useDeleteSpendsMutation>;
export type DeleteSpendsMutationResult = Apollo.MutationResult<DeleteSpendsMutation>;
export type DeleteSpendsMutationOptions = Apollo.BaseMutationOptions<DeleteSpendsMutation, DeleteSpendsMutationVariables>;
export const UpdateUserDocument = gql`
    mutation UpdateUser($input: UserInput!) {
        user(input: $input) {
            id
            username
            fullname
            photo
        }
    }
`;
export type UpdateUserMutationFn = Apollo.MutationFunction<UpdateUserMutation, UpdateUserMutationVariables>;

/**
 * __useUpdateUserMutation__
 *
 * To run a mutation, you first call `useUpdateUserMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateUserMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateUserMutation, { data, loading, error }] = useUpdateUserMutation({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useUpdateUserMutation(baseOptions?: Apollo.MutationHookOptions<UpdateUserMutation, UpdateUserMutationVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useMutation<UpdateUserMutation, UpdateUserMutationVariables>(UpdateUserDocument, options);
}

export type UpdateUserMutationHookResult = ReturnType<typeof useUpdateUserMutation>;
export type UpdateUserMutationResult = Apollo.MutationResult<UpdateUserMutation>;
export type UpdateUserMutationOptions = Apollo.BaseMutationOptions<UpdateUserMutation, UpdateUserMutationVariables>;
export const UpdateFriendshipStatusDocument = gql`
    mutation UpdateFriendshipStatus($input: FriendshipInput!) {
        friendship(input: $input) {
            id
            username
            fullname
            photo
        }
    }
`;
export type UpdateFriendshipStatusMutationFn = Apollo.MutationFunction<UpdateFriendshipStatusMutation, UpdateFriendshipStatusMutationVariables>;

/**
 * __useUpdateFriendshipStatusMutation__
 *
 * To run a mutation, you first call `useUpdateFriendshipStatusMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateFriendshipStatusMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateFriendshipStatusMutation, { data, loading, error }] = useUpdateFriendshipStatusMutation({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useUpdateFriendshipStatusMutation(baseOptions?: Apollo.MutationHookOptions<UpdateFriendshipStatusMutation, UpdateFriendshipStatusMutationVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useMutation<UpdateFriendshipStatusMutation, UpdateFriendshipStatusMutationVariables>(UpdateFriendshipStatusDocument, options);
}

export type UpdateFriendshipStatusMutationHookResult = ReturnType<typeof useUpdateFriendshipStatusMutation>;
export type UpdateFriendshipStatusMutationResult = Apollo.MutationResult<UpdateFriendshipStatusMutation>;
export type UpdateFriendshipStatusMutationOptions = Apollo.BaseMutationOptions<UpdateFriendshipStatusMutation, UpdateFriendshipStatusMutationVariables>;
export const CurrenciesDocument = gql`
    query Currencies {
        currencies {
            currency
        }
    }
`;

/**
 * __useCurrenciesQuery__
 *
 * To run a query within a React component, call `useCurrenciesQuery` and pass it any options that fit your needs.
 * When your component renders, `useCurrenciesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useCurrenciesQuery({
 *   variables: {
 *   },
 * });
 */
export function useCurrenciesQuery(baseOptions?: Apollo.QueryHookOptions<CurrenciesQuery, CurrenciesQueryVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useQuery<CurrenciesQuery, CurrenciesQueryVariables>(CurrenciesDocument, options);
}

export function useCurrenciesLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<CurrenciesQuery, CurrenciesQueryVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useLazyQuery<CurrenciesQuery, CurrenciesQueryVariables>(CurrenciesDocument, options);
}

export function useCurrenciesSuspenseQuery(baseOptions?: Apollo.SkipToken | Apollo.SuspenseQueryHookOptions<CurrenciesQuery, CurrenciesQueryVariables>) {
    const options = baseOptions === Apollo.skipToken ? baseOptions : {...defaultOptions, ...baseOptions}
    return Apollo.useSuspenseQuery<CurrenciesQuery, CurrenciesQueryVariables>(CurrenciesDocument, options);
}

export type CurrenciesQueryHookResult = ReturnType<typeof useCurrenciesQuery>;
export type CurrenciesLazyQueryHookResult = ReturnType<typeof useCurrenciesLazyQuery>;
export type CurrenciesSuspenseQueryHookResult = ReturnType<typeof useCurrenciesSuspenseQuery>;
export type CurrenciesQueryResult = Apollo.QueryResult<CurrenciesQuery, CurrenciesQueryVariables>;
export const SessionDocument = gql`
    query Session {
        session {
            username
        }
    }
`;

/**
 * __useSessionQuery__
 *
 * To run a query within a React component, call `useSessionQuery` and pass it any options that fit your needs.
 * When your component renders, `useSessionQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useSessionQuery({
 *   variables: {
 *   },
 * });
 */
export function useSessionQuery(baseOptions?: Apollo.QueryHookOptions<SessionQuery, SessionQueryVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useQuery<SessionQuery, SessionQueryVariables>(SessionDocument, options);
}

export function useSessionLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<SessionQuery, SessionQueryVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useLazyQuery<SessionQuery, SessionQueryVariables>(SessionDocument, options);
}

export function useSessionSuspenseQuery(baseOptions?: Apollo.SkipToken | Apollo.SuspenseQueryHookOptions<SessionQuery, SessionQueryVariables>) {
    const options = baseOptions === Apollo.skipToken ? baseOptions : {...defaultOptions, ...baseOptions}
    return Apollo.useSuspenseQuery<SessionQuery, SessionQueryVariables>(SessionDocument, options);
}

export type SessionQueryHookResult = ReturnType<typeof useSessionQuery>;
export type SessionLazyQueryHookResult = ReturnType<typeof useSessionLazyQuery>;
export type SessionSuspenseQueryHookResult = ReturnType<typeof useSessionSuspenseQuery>;
export type SessionQueryResult = Apollo.QueryResult<SessionQuery, SessionQueryVariables>;
export const SpendsDocument = gql`
    query Spends($page: Int!, $size: Int!, $sort: [String!], $searchQuery: String, $filterPeriod: FilterPeriod, $filterCurrency: CurrencyValues) {
        spends(
            page: $page
            size: $size
            sort: $sort
            searchQuery: $searchQuery
            filterPeriod: $filterPeriod
            filterCurrency: $filterCurrency
        ) {
            edges {
                node {
                    id
                    spendDate
                    category {
                        id
                        name
                    }
                    currency
                    amount
                    description
                }
            }
            pageInfo {
                hasPreviousPage
                hasNextPage
            }
        }
    }
`;

/**
 * __useSpendsQuery__
 *
 * To run a query within a React component, call `useSpendsQuery` and pass it any options that fit your needs.
 * When your component renders, `useSpendsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useSpendsQuery({
 *   variables: {
 *      page: // value for 'page'
 *      size: // value for 'size'
 *      sort: // value for 'sort'
 *      searchQuery: // value for 'searchQuery'
 *      filterPeriod: // value for 'filterPeriod'
 *      filterCurrency: // value for 'filterCurrency'
 *   },
 * });
 */
export function useSpendsQuery(baseOptions: Apollo.QueryHookOptions<SpendsQuery, SpendsQueryVariables> & ({
    variables: SpendsQueryVariables;
    skip?: boolean;
} | { skip: boolean; })) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useQuery<SpendsQuery, SpendsQueryVariables>(SpendsDocument, options);
}

export function useSpendsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<SpendsQuery, SpendsQueryVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useLazyQuery<SpendsQuery, SpendsQueryVariables>(SpendsDocument, options);
}

export function useSpendsSuspenseQuery(baseOptions?: Apollo.SkipToken | Apollo.SuspenseQueryHookOptions<SpendsQuery, SpendsQueryVariables>) {
    const options = baseOptions === Apollo.skipToken ? baseOptions : {...defaultOptions, ...baseOptions}
    return Apollo.useSuspenseQuery<SpendsQuery, SpendsQueryVariables>(SpendsDocument, options);
}

export type SpendsQueryHookResult = ReturnType<typeof useSpendsQuery>;
export type SpendsLazyQueryHookResult = ReturnType<typeof useSpendsLazyQuery>;
export type SpendsSuspenseQueryHookResult = ReturnType<typeof useSpendsSuspenseQuery>;
export type SpendsQueryResult = Apollo.QueryResult<SpendsQuery, SpendsQueryVariables>;
export const SpendDocument = gql`
    query Spend($id: ID!) {
        spend(id: $id) {
            id
            spendDate
            category {
                name
            }
            currency
            amount
            description
        }
    }
`;

/**
 * __useSpendQuery__
 *
 * To run a query within a React component, call `useSpendQuery` and pass it any options that fit your needs.
 * When your component renders, `useSpendQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useSpendQuery({
 *   variables: {
 *      id: // value for 'id'
 *   },
 * });
 */
export function useSpendQuery(baseOptions: Apollo.QueryHookOptions<SpendQuery, SpendQueryVariables> & ({
    variables: SpendQueryVariables;
    skip?: boolean;
} | { skip: boolean; })) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useQuery<SpendQuery, SpendQueryVariables>(SpendDocument, options);
}

export function useSpendLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<SpendQuery, SpendQueryVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useLazyQuery<SpendQuery, SpendQueryVariables>(SpendDocument, options);
}

export function useSpendSuspenseQuery(baseOptions?: Apollo.SkipToken | Apollo.SuspenseQueryHookOptions<SpendQuery, SpendQueryVariables>) {
    const options = baseOptions === Apollo.skipToken ? baseOptions : {...defaultOptions, ...baseOptions}
    return Apollo.useSuspenseQuery<SpendQuery, SpendQueryVariables>(SpendDocument, options);
}

export type SpendQueryHookResult = ReturnType<typeof useSpendQuery>;
export type SpendLazyQueryHookResult = ReturnType<typeof useSpendLazyQuery>;
export type SpendSuspenseQueryHookResult = ReturnType<typeof useSpendSuspenseQuery>;
export type SpendQueryResult = Apollo.QueryResult<SpendQuery, SpendQueryVariables>;
export const StatDocument = gql`
    query Stat($filterPeriod: FilterPeriod, $filterCurrency: CurrencyValues, $statCurrency: CurrencyValues) {
        stat(
            filterPeriod: $filterPeriod
            filterCurrency: $filterCurrency
            statCurrency: $statCurrency
        ) {
            total
            currency
            statByCategories {
                categoryName
                currency
                sum
                firstSpendDate
                lastSpendDate
            }
        }
    }
`;

/**
 * __useStatQuery__
 *
 * To run a query within a React component, call `useStatQuery` and pass it any options that fit your needs.
 * When your component renders, `useStatQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useStatQuery({
 *   variables: {
 *      filterPeriod: // value for 'filterPeriod'
 *      filterCurrency: // value for 'filterCurrency'
 *      statCurrency: // value for 'statCurrency'
 *   },
 * });
 */
export function useStatQuery(baseOptions?: Apollo.QueryHookOptions<StatQuery, StatQueryVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useQuery<StatQuery, StatQueryVariables>(StatDocument, options);
}

export function useStatLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<StatQuery, StatQueryVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useLazyQuery<StatQuery, StatQueryVariables>(StatDocument, options);
}

export function useStatSuspenseQuery(baseOptions?: Apollo.SkipToken | Apollo.SuspenseQueryHookOptions<StatQuery, StatQueryVariables>) {
    const options = baseOptions === Apollo.skipToken ? baseOptions : {...defaultOptions, ...baseOptions}
    return Apollo.useSuspenseQuery<StatQuery, StatQueryVariables>(StatDocument, options);
}

export type StatQueryHookResult = ReturnType<typeof useStatQuery>;
export type StatLazyQueryHookResult = ReturnType<typeof useStatLazyQuery>;
export type StatSuspenseQueryHookResult = ReturnType<typeof useStatSuspenseQuery>;
export type StatQueryResult = Apollo.QueryResult<StatQuery, StatQueryVariables>;
export const CurrentUserDocument = gql`
    query CurrentUser {
        user {
            id
            username
            fullname
            photo
            categories {
                id
                name
                archived
            }
        }
    }
`;

/**
 * __useCurrentUserQuery__
 *
 * To run a query within a React component, call `useCurrentUserQuery` and pass it any options that fit your needs.
 * When your component renders, `useCurrentUserQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useCurrentUserQuery({
 *   variables: {
 *   },
 * });
 */
export function useCurrentUserQuery(baseOptions?: Apollo.QueryHookOptions<CurrentUserQuery, CurrentUserQueryVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useQuery<CurrentUserQuery, CurrentUserQueryVariables>(CurrentUserDocument, options);
}

export function useCurrentUserLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<CurrentUserQuery, CurrentUserQueryVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useLazyQuery<CurrentUserQuery, CurrentUserQueryVariables>(CurrentUserDocument, options);
}

export function useCurrentUserSuspenseQuery(baseOptions?: Apollo.SkipToken | Apollo.SuspenseQueryHookOptions<CurrentUserQuery, CurrentUserQueryVariables>) {
    const options = baseOptions === Apollo.skipToken ? baseOptions : {...defaultOptions, ...baseOptions}
    return Apollo.useSuspenseQuery<CurrentUserQuery, CurrentUserQueryVariables>(CurrentUserDocument, options);
}

export type CurrentUserQueryHookResult = ReturnType<typeof useCurrentUserQuery>;
export type CurrentUserLazyQueryHookResult = ReturnType<typeof useCurrentUserLazyQuery>;
export type CurrentUserSuspenseQueryHookResult = ReturnType<typeof useCurrentUserSuspenseQuery>;
export type CurrentUserQueryResult = Apollo.QueryResult<CurrentUserQuery, CurrentUserQueryVariables>;
export const CategoriesDocument = gql`
    query Categories {
        user {
            categories {
                id
                name
                archived
            }
        }
    }
`;

/**
 * __useCategoriesQuery__
 *
 * To run a query within a React component, call `useCategoriesQuery` and pass it any options that fit your needs.
 * When your component renders, `useCategoriesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useCategoriesQuery({
 *   variables: {
 *   },
 * });
 */
export function useCategoriesQuery(baseOptions?: Apollo.QueryHookOptions<CategoriesQuery, CategoriesQueryVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useQuery<CategoriesQuery, CategoriesQueryVariables>(CategoriesDocument, options);
}

export function useCategoriesLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<CategoriesQuery, CategoriesQueryVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useLazyQuery<CategoriesQuery, CategoriesQueryVariables>(CategoriesDocument, options);
}

export function useCategoriesSuspenseQuery(baseOptions?: Apollo.SkipToken | Apollo.SuspenseQueryHookOptions<CategoriesQuery, CategoriesQueryVariables>) {
    const options = baseOptions === Apollo.skipToken ? baseOptions : {...defaultOptions, ...baseOptions}
    return Apollo.useSuspenseQuery<CategoriesQuery, CategoriesQueryVariables>(CategoriesDocument, options);
}

export type CategoriesQueryHookResult = ReturnType<typeof useCategoriesQuery>;
export type CategoriesLazyQueryHookResult = ReturnType<typeof useCategoriesLazyQuery>;
export type CategoriesSuspenseQueryHookResult = ReturnType<typeof useCategoriesSuspenseQuery>;
export type CategoriesQueryResult = Apollo.QueryResult<CategoriesQuery, CategoriesQueryVariables>;
export const FriendsDocument = gql`
    query Friends($page: Int!, $size: Int!, $sort: [String!], $searchQuery: String) {
        user {
            friends(page: $page, size: $size, sort: $sort, searchQuery: $searchQuery) {
                edges {
                    node {
                        id
                        username
                        photoSmall
                        fullname
                        friendshipStatus
                    }
                }
                pageInfo {
                    hasPreviousPage
                    hasNextPage
                }
            }
        }
    }
`;

/**
 * __useFriendsQuery__
 *
 * To run a query within a React component, call `useFriendsQuery` and pass it any options that fit your needs.
 * When your component renders, `useFriendsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useFriendsQuery({
 *   variables: {
 *      page: // value for 'page'
 *      size: // value for 'size'
 *      sort: // value for 'sort'
 *      searchQuery: // value for 'searchQuery'
 *   },
 * });
 */
export function useFriendsQuery(baseOptions: Apollo.QueryHookOptions<FriendsQuery, FriendsQueryVariables> & ({
    variables: FriendsQueryVariables;
    skip?: boolean;
} | { skip: boolean; })) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useQuery<FriendsQuery, FriendsQueryVariables>(FriendsDocument, options);
}

export function useFriendsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<FriendsQuery, FriendsQueryVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useLazyQuery<FriendsQuery, FriendsQueryVariables>(FriendsDocument, options);
}

export function useFriendsSuspenseQuery(baseOptions?: Apollo.SkipToken | Apollo.SuspenseQueryHookOptions<FriendsQuery, FriendsQueryVariables>) {
    const options = baseOptions === Apollo.skipToken ? baseOptions : {...defaultOptions, ...baseOptions}
    return Apollo.useSuspenseQuery<FriendsQuery, FriendsQueryVariables>(FriendsDocument, options);
}

export type FriendsQueryHookResult = ReturnType<typeof useFriendsQuery>;
export type FriendsLazyQueryHookResult = ReturnType<typeof useFriendsLazyQuery>;
export type FriendsSuspenseQueryHookResult = ReturnType<typeof useFriendsSuspenseQuery>;
export type FriendsQueryResult = Apollo.QueryResult<FriendsQuery, FriendsQueryVariables>;
export const AllPeopleDocument = gql`
    query AllPeople($page: Int!, $size: Int!, $sort: [String!], $searchQuery: String) {
        allPeople(page: $page, size: $size, sort: $sort, searchQuery: $searchQuery) {
            edges {
                node {
                    id
                    username
                    photoSmall
                    fullname
                    friendshipStatus
                }
            }
            pageInfo {
                hasPreviousPage
                hasNextPage
            }
        }
    }
`;

/**
 * __useAllPeopleQuery__
 *
 * To run a query within a React component, call `useAllPeopleQuery` and pass it any options that fit your needs.
 * When your component renders, `useAllPeopleQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useAllPeopleQuery({
 *   variables: {
 *      page: // value for 'page'
 *      size: // value for 'size'
 *      sort: // value for 'sort'
 *      searchQuery: // value for 'searchQuery'
 *   },
 * });
 */
export function useAllPeopleQuery(baseOptions: Apollo.QueryHookOptions<AllPeopleQuery, AllPeopleQueryVariables> & ({
    variables: AllPeopleQueryVariables;
    skip?: boolean;
} | { skip: boolean; })) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useQuery<AllPeopleQuery, AllPeopleQueryVariables>(AllPeopleDocument, options);
}

export function useAllPeopleLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<AllPeopleQuery, AllPeopleQueryVariables>) {
    const options = {...defaultOptions, ...baseOptions}
    return Apollo.useLazyQuery<AllPeopleQuery, AllPeopleQueryVariables>(AllPeopleDocument, options);
}

export function useAllPeopleSuspenseQuery(baseOptions?: Apollo.SkipToken | Apollo.SuspenseQueryHookOptions<AllPeopleQuery, AllPeopleQueryVariables>) {
    const options = baseOptions === Apollo.skipToken ? baseOptions : {...defaultOptions, ...baseOptions}
    return Apollo.useSuspenseQuery<AllPeopleQuery, AllPeopleQueryVariables>(AllPeopleDocument, options);
}

export type AllPeopleQueryHookResult = ReturnType<typeof useAllPeopleQuery>;
export type AllPeopleLazyQueryHookResult = ReturnType<typeof useAllPeopleLazyQuery>;
export type AllPeopleSuspenseQueryHookResult = ReturnType<typeof useAllPeopleSuspenseQuery>;
export type AllPeopleQueryResult = Apollo.QueryResult<AllPeopleQuery, AllPeopleQueryVariables>;