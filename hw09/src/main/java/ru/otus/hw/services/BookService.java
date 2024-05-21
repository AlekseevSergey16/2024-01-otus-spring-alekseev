package ru.otus.hw.services;

import ru.otus.hw.dto.book.BookCreateDto;
import ru.otus.hw.dto.book.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    BookDto create(BookCreateDto bookDto);

    BookDto update(long id, BookCreateDto bookDto);

    void deleteById(long id);
}
