package ru.otus.hw.controllers.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.comment.CommentCreateDto;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CommentSummaryDto;
import ru.otus.hw.dto.comment.CommentUpdateDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody @Valid CommentCreateDto commentDto) {
        return commentService.create(commentDto);
    }

    @PutMapping("/api/comments/{id}")
    public CommentDto updateComment(@RequestBody @Valid CommentUpdateDto commentDto) {
        return commentService.update(commentDto.getId(), commentDto.getText());
    }

    @GetMapping("/api/comments/{id}")
    public CommentDto getCommentById(@PathVariable long id) {
        return commentService.findById(id);
    }

    @GetMapping("/api/comments")
    public List<CommentSummaryDto> getCommentsByBookId(@RequestParam long bookId) {
        return commentService.findByBookId(bookId);
    }

    @DeleteMapping("/api/comment/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable long id) {
        commentService.deleteById(id);
    }

}
