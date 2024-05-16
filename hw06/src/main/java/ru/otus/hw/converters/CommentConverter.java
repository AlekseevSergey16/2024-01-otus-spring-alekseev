package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CommentSummaryDto;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    public String commentToString(CommentDto comment) {
        return "Id: %d, Text: %s, BookId: %d, BookTitle: %s".formatted(
                comment.id(), comment.text(), comment.bookId(), comment.bookTitle());
    }

    public String commentSummaryToString(CommentSummaryDto commentSummary) {
        return "Id: %d, Text: %s".formatted(commentSummary.id(), commentSummary.text());
    }

}
