package ru.otus.hw.dto.comment;

public record CommentDto(long id, String text, long bookId, String bookTitle) {
}
