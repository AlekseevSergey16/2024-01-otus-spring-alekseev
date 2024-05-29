package ru.otus.hw.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.comment.CommentCreateDto;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CommentMapper;
import ru.otus.hw.dto.comment.CommentSummaryDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Transactional
    @Override
    public CommentDto create(CommentCreateDto commentDto) {
        Book book = bookRepository.findById(commentDto.getBookId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id %d not found".formatted(commentDto.getBookId())));

        Comment comment = new Comment(0, commentDto.getText(), book);

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto update(long id, String text) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        comment.setText(text);

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto findById(long id) {
        return commentMapper.toDto(commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id))));
    }

    @Override
    public List<CommentSummaryDto> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId).stream()
                .map(commentMapper::toSummaryDto)
                .toList();
    }

    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
