package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.book.BookCreateDto;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.BookSummaryDto;
import ru.otus.hw.dto.book.BookUpdateDto;

import java.util.List;

public interface BookService {
    Mono<BookSummaryDto> findById(String id);

    Flux<BookDto> findAll();

    Mono<BookDto> create(BookCreateDto bookDto);

    Mono<BookDto> update(BookUpdateDto bookDto);

    Mono<Void> deleteById(String id);
}
