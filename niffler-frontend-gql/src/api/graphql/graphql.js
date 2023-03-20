import {ApolloClient, createHttpLink, InMemoryCache} from "@apollo/client";
import {setContext} from "@apollo/client/link/context";

const httpLink = createHttpLink({
    uri: `${process.env.REACT_APP_GATEWAY_URL}/graphql`,
});

const authLink = setContext((_, {headers}) => {
    const token = sessionStorage.getItem('id_token');
    return {
        headers: {
            ...headers,
            authorization: token ? `Bearer ${token}` : "",
        }
    }
});

export const qraphQlClient = new ApolloClient({
    link: authLink.concat(httpLink),
    cache: new InMemoryCache()
});
