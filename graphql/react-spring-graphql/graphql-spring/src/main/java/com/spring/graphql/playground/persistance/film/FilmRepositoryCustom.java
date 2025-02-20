package com.spring.graphql.playground.persistance.film;

import java.util.List;

public interface FilmRepositoryCustom {
    List<FilmEntity> findAllByIdGt(Long cursor, Long limit);
}
