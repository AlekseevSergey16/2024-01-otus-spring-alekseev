package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by bookId", key = "cbbid")
    public String findCommentByBookId(long bookId) {
        return commentService.findByBookId(bookId).stream()
                .map(commentConverter::commentSummaryToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find comment by bookId", key = "cbid")
    public String findCommentById(long id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Comment with id %d not found".formatted(id));
    }

    //cins привет 2
    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String text, long bookId) {
        return commentConverter.commentToString(commentService.insert(text, bookId));
    }

    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(long id, String text) {
        return commentConverter.commentToString(commentService.update(id, text));
    }

}
