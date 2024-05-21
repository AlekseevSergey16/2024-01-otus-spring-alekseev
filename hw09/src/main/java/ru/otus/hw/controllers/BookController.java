package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.book.BookCreateDto;
import ru.otus.hw.dto.genre.GenreDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping
    public String listPage(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "book-list-page";
    }

    @GetMapping("/books")
    public String createPage(Model model) {
        model.addAttribute("book", new BookCreateDto());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "book-form";
    }

    @GetMapping("/books/{id}")
    public String editPage(@PathVariable long id, Model model) {
        BookDto book = bookService.findById(id);
        BookCreateDto bookDto = new BookCreateDto(book.id(), book.title(), book.author().id(), book.genres().stream()
                .map(GenreDto::id).collect(Collectors.toSet()));
        model.addAttribute("book", bookDto);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "book-form";
    }

    @PostMapping("/books")
    public String createBook(@ModelAttribute @Valid BookCreateDto book) {
        bookService.create(book);
        return "redirect:/";
    }

    @PostMapping("/books/{id}")
    public String editBook(@PathVariable long id, @ModelAttribute @Valid BookCreateDto book) {
        bookService.update(id, book);
        return "redirect:/";
    }

    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }

}
