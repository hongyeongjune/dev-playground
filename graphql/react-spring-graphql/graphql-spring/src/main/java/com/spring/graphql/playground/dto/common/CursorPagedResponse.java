package com.spring.graphql.playground.dto.common;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CursorPagedResponse <T> {
    private Long cursor;
    private List<T> data;

    public static <T> CursorPagedResponse<T> of(Long cursor, List<T> data) {
        return CursorPagedResponse.<T>builder()
            .cursor(cursor)
            .data(data)
            .build();
    }

    public static <T> CursorPagedResponse<T> newInstance() {
        return new CursorPagedResponse<>();
    }
}
