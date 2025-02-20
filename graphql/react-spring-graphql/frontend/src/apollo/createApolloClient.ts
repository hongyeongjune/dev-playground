import { ApolloClient, NormalizedCacheObject } from '@apollo/client';
import { createApolloCache } from './createApolloCache';

export const createApolloClient = (): ApolloClient<NormalizedCacheObject> => {
    return new ApolloClient({
        uri: 'http://localhost:8080/graphql',
        cache: createApolloCache(),
    });
};