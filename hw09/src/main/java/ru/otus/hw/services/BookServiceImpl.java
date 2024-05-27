package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public BookSummaryDto findById(long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toSummaryDto)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public BookDto create(BookCreateDto bookDto) {
        List<Genre> genres = genreRepository.findAllById(bookDto.getGenreIds());
        if (genres.isEmpty() || bookDto.getGenreIds().size() != genres.size()) {
            throw new EntityNotFoundException(
                    "One or all genres with ids %s not found".formatted(bookDto.getGenreIds()));
        }

        Author author = authorRepository.findById(bookDto.getAuthorId()).orElseThrow(() ->
                new EntityNotFoundException("Author with id %d not found".formatted(bookDto.getAuthorId())));

        Book book = bookRepository.save(Book.builder()
                .title(bookDto.getTitle())
                .author(author)
                .genres(new HashSet<>(genres))
                .build());

        return bookMapper.toDto(book);
    }

    @Transactional
    @Override
    public BookDto update(BookUpdateDto bookDto) {
        Book book = bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookDto.getId())));

        List<Genre> genres = genreRepository.findAllById(bookDto.getGenreIds());
        if (genres.isEmpty() || bookDto.getGenreIds().size() != genres.size()) {
            throw new EntityNotFoundException(
                    "One or all genres with ids %s not found".formatted(bookDto.getGenreIds()));
        }

        Author author = authorRepository.findById(bookDto.getAuthorId()).orElseThrow(() ->
                new EntityNotFoundException("Author with id %d not found".formatted(bookDto.getAuthorId())));

        book.setTitle(bookDto.getTitle());
        book.setAuthor(author);
        book.setGenres(new HashSet<>(genres));

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

}
