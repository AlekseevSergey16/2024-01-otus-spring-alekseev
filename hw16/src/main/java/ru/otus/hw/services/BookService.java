package ru.otus.hw.services;

import ru.otus.hw.dto.book.BookCreateDto;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.BookSummaryDto;
import ru.otus.hw.dto.book.BookUpdateDto;

import java.util.List;

public interface BookService {
    BookSummaryDto findById(long id);

    List<BookDto> findAll();

    BookDto create(BookCreateDto bookDto);

    BookDto update(BookUpdateDto bookDto);

    void deleteById(long id);
}
