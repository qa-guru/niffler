import {ApolloClient, createHttpLink, InMemoryCache} from "@apollo/client";
import {setContext} from "@apollo/client/link/context";
import {idTokenFromLocalStorage} from "./authUtils.ts";

const BASE_URL = `${import.meta.env.VITE_API_URL}`;

const apolloHttpLink = createHttpLink({
    uri: `${BASE_URL}/graphql`,
})

const headerLink = setContext((_request, previousContext) => ({
    headers: {
        ...previousContext.headers,
        "Authorization": idTokenFromLocalStorage() ? `Bearer ${idTokenFromLocalStorage()}` : "",
    },
}));


const client = new ApolloClient({
    link: headerLink.concat(apolloHttpLink),
    cache: new InMemoryCache(),
});

export default client;
