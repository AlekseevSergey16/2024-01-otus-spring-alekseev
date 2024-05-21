package ru.otus.hw.services;

import ru.otus.hw.dto.book.BookCreateDto;
import ru.otus.hw.dto.book.BookDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto create(BookCreateDto bookDto);

    BookDto update(long id, BookCreateDto bookDto);

    void deleteById(long id);
}
