package ru.otus.hw.controllers.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.book.BookCreateDto;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.BookSummaryDto;
import ru.otus.hw.dto.book.BookUpdateDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/books")
    public BookDto createBook(@RequestBody @Valid BookCreateDto bookDto) {
        return bookService.create(bookDto);
    }

    @PutMapping("/api/books/{id}")
    public BookDto updateBook(@RequestBody @Valid BookUpdateDto bookDto) {
        return bookService.update(bookDto);
    }

    @GetMapping("/api/books")
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/books/{id}")
    public BookSummaryDto getBookById(@PathVariable long id) {
        return bookService.findById(id);
    }

    @DeleteMapping("/api/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
    }

}
