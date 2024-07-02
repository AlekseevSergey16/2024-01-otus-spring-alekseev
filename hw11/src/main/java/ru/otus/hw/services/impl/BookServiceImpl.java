package ru.otus.hw.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.book.BookCreateDto;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.BookMapper;
import ru.otus.hw.dto.book.BookSummaryDto;
import ru.otus.hw.dto.book.BookUpdateDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.BookService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final GenreRepository genreRepository;

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public Mono<BookSummaryDto> findById(String id) {
        return bookRepository.findById(id)
                .map(bookMapper::toSummaryDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAll().map(bookMapper::toDto);
    }

    @Transactional
    @Override
    public Mono<BookDto> create(BookCreateDto bookDto) {
        Mono<List<Genre>> genreListMono = genreRepository.findAllById(bookDto.getGenreIds()).collectList();
        Mono<Optional<Author>> authorMono = authorRepository.findById(bookDto.getAuthorId()).map(Optional::ofNullable);

        return Mono.zip(genreListMono, authorMono)
                .flatMap(tuple -> {
                    List<Genre> genres = tuple.getT1();
                    if (genres.isEmpty() || bookDto.getGenreIds().size() != genres.size()) {
                        return Mono.error(new EntityNotFoundException(
                                "One or all genres with ids %s not found".formatted(bookDto.getGenreIds())));
                    }

                    Optional<Author> author = tuple.getT2();
                    if (author.isEmpty()) {
                        return Mono.error(new EntityNotFoundException(
                                "Author with id %s not found".formatted(bookDto.getAuthorId())));
                    }

                    return bookRepository.save(Book.builder()
                                    .title(bookDto.getTitle())
                                    .authorId(author.get().getId())
                                    .genreIds(genres.stream().map(Genre::getId).toList())
                                    .build());
                })
                .map(bookMapper::toDto);

//        return bookRepository.save(Book.builder()
//                        .title(bookDto.getTitle())
//                        .authorId(bookDto.getAuthorId())
//                        .genreIds(new ArrayList<>(bookDto.getGenreIds()))
//                        .build())
//                .map(bookMapper::toDto);
    }

    @Transactional
    @Override
    public Mono<BookDto> update(BookUpdateDto bookDto) {
        return bookRepository.findById(bookDto.getId())
                .map(book -> book.toBuilder()
                        .title(bookDto.getTitle())
                        .authorId(bookDto.getAuthorId())
                        .genreIds(new ArrayList<>(bookDto.getGenreIds()))
                        .build())
                .flatMap(bookRepository::save)
                .map(bookMapper::toDto);
    }

    @Transactional
    @Override
    public Mono<Void> deleteById(String id) {
        return bookRepository.deleteById(id);
    }

}
