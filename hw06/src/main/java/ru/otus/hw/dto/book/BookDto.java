package ru.otus.hw.dto.book;

import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.dto.genre.GenreDto;

import java.util.List;
import java.util.Set;

public record BookDto(long id, String title, AuthorDto author, List<GenreDto> genres) {
}
