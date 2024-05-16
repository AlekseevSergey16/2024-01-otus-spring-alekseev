package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Getter
@Document("authors")
public final class Author {

    @Id
    private final String id;

    private final String fullName;

}