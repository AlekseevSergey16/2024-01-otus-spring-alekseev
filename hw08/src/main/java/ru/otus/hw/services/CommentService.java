package ru.otus.hw.services;

import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CommentSummaryDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    CommentDto insert(String text, String bookId);

    CommentDto update(String id, String text);

    Optional<CommentDto> findById(String id);

    List<CommentSummaryDto> findByBookId(String bookId);

    void deleteById(String id);

}
