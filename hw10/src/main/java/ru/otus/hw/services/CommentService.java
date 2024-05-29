package ru.otus.hw.services;

import ru.otus.hw.dto.comment.CommentCreateDto;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CommentSummaryDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    CommentDto create(CommentCreateDto commentDto);

    CommentDto update(long id, String text);

    CommentDto findById(long id);

    List<CommentSummaryDto> findByBookId(long bookId);

    void deleteById(long id);

}
