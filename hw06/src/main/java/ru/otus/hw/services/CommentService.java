package ru.otus.hw.services;

import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CommentSummaryDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    CommentDto insert(String text, long bookId);

    CommentDto update(long id, String text);

    Optional<CommentDto> findById(long id);

    List<CommentSummaryDto> findByBookId(long bookId);

}
