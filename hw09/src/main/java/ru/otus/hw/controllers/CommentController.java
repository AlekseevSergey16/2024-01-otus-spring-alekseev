package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.comment.CommentSummaryDto;
import ru.otus.hw.dto.comment.CommentCreateDto;
import ru.otus.hw.dto.comment.CommentUpdateDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/books/{bookId}/comments")
    public String commentListByBookPage(@PathVariable long bookId, Model model) {
        List<CommentSummaryDto> comments = commentService.findByBookId(bookId);
        model.addAttribute("comments", comments);
        model.addAttribute("bookId", bookId);
        model.addAttribute("createComment", new CommentCreateDto());
        return "book-comment-list-page";
    }

    @PostMapping("/books/{bookId}/comments")
    public String createComment(@PathVariable long bookId, @ModelAttribute CommentCreateDto comment) {
        commentService.insert(comment.getText(), bookId);
        return "redirect:/books/%s/comments".formatted(comment.getBookId());
    }

    @PostMapping("/books/{bookId}/comments/{id}")
    public String editComment(@PathVariable long bookId, @PathVariable long id, @RequestParam String updatedText) {
        commentService.update(id, updatedText);
        return "redirect:/books/%s/comments".formatted(bookId);
    }

}
