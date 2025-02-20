package com.spring.graphql.playground.film;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spring.graphql.playground.dto.common.CursorPagedResponse;
import com.spring.graphql.playground.dto.film.FilmResponse;
import com.spring.graphql.playground.service.FilmService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @QueryMapping
    public FilmResponse getFilm(@Argument Long filmId) {
        return filmService.getFilm(filmId);
    }

    @QueryMapping
    public CursorPagedResponse<FilmResponse> getFilms(@Argument Long cursor, @Argument Long limit) {
        return filmService.getCursorPagedFilms(cursor, limit);
    }
}
