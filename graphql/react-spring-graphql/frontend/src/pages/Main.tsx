import React from 'react';
import { Heading } from '@chakra-ui/react';
import FilmList from '../components/film/FilmList';
import CommonLayout from '../components/CommonLayout';

const Main = () => {
    return (
        <CommonLayout>
            <Heading size="1g">최고의 장면을 찾아보세요.</Heading>
            <FilmList />
        </CommonLayout>
    )
}

export default Main;