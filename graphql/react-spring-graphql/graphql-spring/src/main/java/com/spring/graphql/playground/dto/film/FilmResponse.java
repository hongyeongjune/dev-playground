package com.spring.graphql.playground.dto.film;

import java.time.LocalDate;

import com.spring.graphql.playground.dto.director.DirectorResponse;
import com.spring.graphql.playground.persistance.film.FilmEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmResponse {
    private Long id;
    private String title;
    private String subtitle;
    private String genre;
    private Double runningTime;
    private String description;
    private String posterImg;
    private LocalDate releaseDate;
    private DirectorResponse director;

    public static FilmResponse from(FilmEntity filmEntity) {
        return FilmResponse.builder()
            .id(filmEntity.getId())
            .title(filmEntity.getTitle())
            .subtitle(filmEntity.getSubtitle())
            .genre(filmEntity.getGenre())
            .runningTime(filmEntity.getRunningTime())
            .description(filmEntity.getDescription())
            .posterImg(filmEntity.getPosterImg())
            .releaseDate(filmEntity.getReleaseDate())
            .director(DirectorResponse.from(filmEntity.getDirectorEntity()))
            .build();
    }
}
