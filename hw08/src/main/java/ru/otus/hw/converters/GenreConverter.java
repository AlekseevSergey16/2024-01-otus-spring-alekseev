package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.genre.GenreDto;

@Component
public class GenreConverter {
    public String genreToString(GenreDto genre) {
        return "Id: %s, Name: %s".formatted(genre.id(), genre.name());
    }
}
