package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Getter
@Builder
@Document("comments")
public final class Comment {
    private final String id;

    private final String text;

    @DBRef
    private final Book book;

    public Comment withText(String text) {
        return new Comment(this.id, text, this.book);
    }

}