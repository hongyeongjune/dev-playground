package com.spring.graphql.playground.dto.director;

import com.spring.graphql.playground.persistance.director.DirectorEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DirectorResponse {
    private Long id;
    private String name;

    public static DirectorResponse from(DirectorEntity directorEntity) {
        return DirectorResponse.builder()
            .id(directorEntity.getId())
            .name(directorEntity.getName())
            .build();
    }
}
