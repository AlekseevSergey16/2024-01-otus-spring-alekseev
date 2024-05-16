package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@Document("books")
public final class Book {

    @Id
    private final String id;

    private final String title;

    @DBRef
    private final Author author;

    @DBRef(lazy = true)
    private final List<Genre> genres;

}
