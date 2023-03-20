import {ApolloProvider} from "@apollo/client";
import React from 'react';
import ReactDOM from 'react-dom/client';
import './../public/styles/index.css';
import {BrowserRouter} from "react-router-dom";
import {qraphQlClient} from "./api/graphql/graphql";
import App from './App';


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <BrowserRouter>
            <ApolloProvider client={qraphQlClient}>
                <App/>
            </ApolloProvider>
        </BrowserRouter>
    </React.StrictMode>
);
