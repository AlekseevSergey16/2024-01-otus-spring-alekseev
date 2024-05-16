package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Getter
@Document("genres")
public final class Genre {

    @Id
    private final String id;

    private final String name;

}
