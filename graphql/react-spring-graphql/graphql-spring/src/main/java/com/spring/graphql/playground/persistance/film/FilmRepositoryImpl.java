package com.spring.graphql.playground.persistance.film;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.graphql.playground.persistance.director.QDirectorEntity;

public class FilmRepositoryImpl extends QuerydslRepositorySupport implements FilmRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private static final QFilmEntity filmEntity = QFilmEntity.filmEntity;
    private static final QDirectorEntity directorEntity = QDirectorEntity.directorEntity;

    public FilmRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(FilmEntity.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<FilmEntity> findAllByIdGt(Long cursor, Long limit) {
        JPAQuery<FilmEntity> query = jpaQueryFactory.select(filmEntity)
            .from(filmEntity)
            .innerJoin(filmEntity.directorEntity, directorEntity).fetchJoin()
            .orderBy(filmEntity.id.asc());

        if (cursor != null) {
            query.where(filmEntity.id.goe(cursor));
        }
        if (limit == null || limit > 6) {
            query.limit(6);
        } else {
            query.limit(limit);
        }

        return query.fetch();
    }
}
