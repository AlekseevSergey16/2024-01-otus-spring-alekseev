package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
@Document("books")
public class Book {

    @Id
    private final String id;

    private final String title;

    private final String authorId;

    private final List<String> genreIds;

}
