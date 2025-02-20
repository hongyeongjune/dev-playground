import CommonLayout from '../CommonLayout';
import { useParams } from 'react-router-dom';
import { useFilmQuery } from '../../generated/graphql';
import { Box, Spinner, Text } from '@chakra-ui/react';
import FilmDetails from './FilmDetails';

interface FilmParams {
    filmId: string;
}

const Film = () => {
    const { filmId } = useParams<FilmParams>();
    const { data, loading, error } = useFilmQuery({
        variables: {
            filmId: Number(filmId),
        }
    });

    return (
        <CommonLayout>
            {error &&
                <Text>페이지를 표시할 수 없습니다.</Text>
            }
            {loading &&
                <Spinner />
            }
            {filmId && data?.getFilm ? <FilmDetails film={data.getFilm} /> : <Text>페이지를 표시할 수 없습니다.</Text>}
        </CommonLayout>
    )
}

export default Film;