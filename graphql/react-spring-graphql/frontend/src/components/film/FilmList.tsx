import { FilmsQuery, useFilmsQuery } from '../../generated/graphql';
import { Box, SimpleGrid, Skeleton } from '@chakra-ui/react';
import FilmCard from './FilmCard';
import { Waypoint } from 'react-waypoint';

const FilmList =() => {
    const LIMIT = 6;
    const { data, loading, error, fetchMore } = useFilmsQuery({
        variables: {
            cursor: 1,
            limit: LIMIT,
        }
    });
    return <SimpleGrid columns={[2, null, 3]} spacing={[2, null, 10]}>
        {error &&
            <p>{error.message}</p>
        }
        {loading &&
            new Array(6).fill(0).map((x) => <Skeleton key={x} height="400px" />)
        }
        {!loading && data &&
            data.getFilms.data.map((film, index) => (
                <Box key={film.id}>
                    {data.getFilms.cursor && index === data.getFilms.data.length - LIMIT / 2 && (
                        <Waypoint onEnter={() => {
                            fetchMore({
                                variables: {
                                    cursor: data.getFilms.cursor,
                                    limit: LIMIT,
                                },
                            });
                        }} />
                    )}
                    <FilmCard film={film} />
                </Box>
            ))}
    </SimpleGrid>
};

export default FilmList;