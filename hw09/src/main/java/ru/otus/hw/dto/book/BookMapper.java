package ru.otus.hw.dto.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.author.AuthorMapper;
import ru.otus.hw.dto.genre.GenreMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookMapper {

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                authorMapper.toDto(book.getAuthor()),
                book.getGenres().stream()
                        .map(genreMapper::toDto)
                        .toList());
    }

    public BookSummaryDto toSummaryDto(Book book) {
        return new BookSummaryDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                book.getGenres().stream()
                        .map(Genre::getId)
                        .collect(Collectors.toSet())
        );
    }

}
