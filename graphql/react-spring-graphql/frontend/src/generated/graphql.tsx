/* eslint-disable */
import { gql } from '@apollo/client';
import * as Apollo from '@apollo/client';
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
const defaultOptions = {} as const;
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string;
  String: string;
  Boolean: boolean;
  Int: number;
  Float: number;
  Date: any;
  Long: any;
};

export type CursorPagedResponse = {
  __typename?: 'CursorPagedResponse';
  cursor?: Maybe<Scalars['Long']>;
  data: Array<FilmResponse>;
};

export type DirectorResponse = {
  __typename?: 'DirectorResponse';
  id: Scalars['Long'];
  name: Scalars['String'];
};

export type FilmResponse = {
  __typename?: 'FilmResponse';
  description: Scalars['String'];
  director: DirectorResponse;
  genre: Scalars['String'];
  id: Scalars['Long'];
  posterImg: Scalars['String'];
  releaseDate: Scalars['String'];
  runningTime: Scalars['Float'];
  subtitle?: Maybe<Scalars['String']>;
  title: Scalars['String'];
};

export type Query = {
  __typename?: 'Query';
  getFilm: FilmResponse;
  getFilms: CursorPagedResponse;
};


export type QueryGetFilmArgs = {
  filmId: Scalars['Long'];
};


export type QueryGetFilmsArgs = {
  cursor?: InputMaybe<Scalars['Long']>;
  limit?: InputMaybe<Scalars['Long']>;
};

export type FilmQueryVariables = Exact<{
  filmId: Scalars['Long'];
}>;


export type FilmQuery = { __typename?: 'Query', getFilm: { __typename?: 'FilmResponse', id: any, title: string, subtitle?: string | null, description: string, genre: string, runningTime: number, posterImg: string, releaseDate: string, director: { __typename?: 'DirectorResponse', id: any, name: string } } };

export type FilmsQueryVariables = Exact<{
  cursor?: InputMaybe<Scalars['Long']>;
  limit?: InputMaybe<Scalars['Long']>;
}>;


export type FilmsQuery = { __typename?: 'Query', getFilms: { __typename?: 'CursorPagedResponse', cursor?: any | null, data: Array<{ __typename?: 'FilmResponse', id: any, title: string, subtitle?: string | null, genre: string, runningTime: number, releaseDate: string, posterImg: string, director: { __typename?: 'DirectorResponse', id: any, name: string } }> } };


export const FilmDocument = gql`
    query film($filmId: Long!) {
  getFilm(filmId: $filmId) {
    id
    title
    subtitle
    description
    genre
    runningTime
    posterImg
    releaseDate
    director {
      id
      name
    }
  }
}
    `;

/**
 * __useFilmQuery__
 *
 * To run a query within a React component, call `useFilmQuery` and pass it any options that fit your needs.
 * When your component renders, `useFilmQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useFilmQuery({
 *   variables: {
 *      filmId: // value for 'filmId'
 *   },
 * });
 */
export function useFilmQuery(baseOptions: Apollo.QueryHookOptions<FilmQuery, FilmQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<FilmQuery, FilmQueryVariables>(FilmDocument, options);
      }
export function useFilmLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<FilmQuery, FilmQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<FilmQuery, FilmQueryVariables>(FilmDocument, options);
        }
export type FilmQueryHookResult = ReturnType<typeof useFilmQuery>;
export type FilmLazyQueryHookResult = ReturnType<typeof useFilmLazyQuery>;
export type FilmQueryResult = Apollo.QueryResult<FilmQuery, FilmQueryVariables>;
export const FilmsDocument = gql`
    query Films($cursor: Long, $limit: Long) {
  getFilms(cursor: $cursor, limit: $limit) {
    cursor
    data {
      id
      title
      subtitle
      genre
      runningTime
      releaseDate
      posterImg
      director {
        id
        name
      }
    }
  }
}
    `;

/**
 * __useFilmsQuery__
 *
 * To run a query within a React component, call `useFilmsQuery` and pass it any options that fit your needs.
 * When your component renders, `useFilmsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useFilmsQuery({
 *   variables: {
 *      cursor: // value for 'cursor'
 *      limit: // value for 'limit'
 *   },
 * });
 */
export function useFilmsQuery(baseOptions?: Apollo.QueryHookOptions<FilmsQuery, FilmsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<FilmsQuery, FilmsQueryVariables>(FilmsDocument, options);
      }
export function useFilmsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<FilmsQuery, FilmsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<FilmsQuery, FilmsQueryVariables>(FilmsDocument, options);
        }
export type FilmsQueryHookResult = ReturnType<typeof useFilmsQuery>;
export type FilmsLazyQueryHookResult = ReturnType<typeof useFilmsLazyQuery>;
export type FilmsQueryResult = Apollo.QueryResult<FilmsQuery, FilmsQueryVariables>;