package com.spring.graphql.playground.persistance.film;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.graphql.data.GraphQlRepository;

@GraphQlRepository
public interface FilmRepository extends JpaRepository<FilmEntity, Long>, FilmRepositoryCustom {
}
