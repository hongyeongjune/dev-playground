package com.spring.graphql.playground.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.spring.graphql.playground.dto.common.CursorPagedResponse;
import com.spring.graphql.playground.dto.film.FilmResponse;
import com.spring.graphql.playground.persistance.film.FilmEntity;
import com.spring.graphql.playground.persistance.film.FilmRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;

    public FilmResponse getFilm(Long filmId) {
        return FilmResponse.from(
            filmRepository.findById(filmId)
                .orElseThrow(() -> new RuntimeException("Film Entity 가 존재하지 않습니다."))
        );
    }

    public List<FilmResponse> getFilms() {
        return filmRepository.findAll().stream()
            .map(FilmResponse::from)
            .collect(Collectors.toList());
    }

    public CursorPagedResponse<FilmResponse> getCursorPagedFilms(Long cursor, Long limit) {
        if (Objects.isNull(cursor)) {
            return CursorPagedResponse.newInstance();
        }
        final List<FilmEntity> films = filmRepository.findAllByIdGt(cursor, limit);
        long nextCursor = films.get(films.size() - 1).getId() + 1;
        return CursorPagedResponse.of(
            filmRepository.findById(nextCursor).isPresent() ? nextCursor : null,
            films.stream()
                .map(FilmResponse::from)
                .collect(Collectors.toList())
        );
    }
}
