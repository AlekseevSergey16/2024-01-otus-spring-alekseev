package ru.otus.hw.dto.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(), comment.getText(), comment.getBook().getId());
    }

    public CommentSummaryDto toSummaryDto(Comment comment) {
        return new CommentSummaryDto(comment.getId(), comment.getText());
    }

}
