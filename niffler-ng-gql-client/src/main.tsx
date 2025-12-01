import ReactDOM from 'react-dom/client';
import App from './App.tsx';
import {CssBaseline, ThemeProvider} from '@mui/material';
import theme from './theme';
import './index.css';
import {StrictMode} from "react";
import {ApolloProvider} from "@apollo/client";
import client from "./api/graphqlClient.ts";


ReactDOM.createRoot(document.getElementById('root')!).render(
    <ApolloProvider client={client}>
        <StrictMode>
            <ThemeProvider theme={theme}>
                <CssBaseline/>
                <App/>
            </ThemeProvider>
        </StrictMode>
    </ApolloProvider>
);
