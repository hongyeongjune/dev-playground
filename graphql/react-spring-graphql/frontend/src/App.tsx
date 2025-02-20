import React from 'react';
import { ChakraProvider, theme } from '@chakra-ui/react';
import { ApolloProvider } from '@apollo/client';
import { createApolloClient } from './apollo/createApolloClient';
import { BrowserRouter, Route } from 'react-router-dom';
import Main from './pages/Main';
import Film from './components/film/Film';

const apolloClient = createApolloClient();

export const App = () => (
    <ApolloProvider client={apolloClient}>
        <ChakraProvider theme={theme}>
            <BrowserRouter>
                <Route exact path="/" component={Main} />
                <Route exact path="/film/:filmId" component={Film} />
            </BrowserRouter>
        </ChakraProvider>
    </ApolloProvider>
);
