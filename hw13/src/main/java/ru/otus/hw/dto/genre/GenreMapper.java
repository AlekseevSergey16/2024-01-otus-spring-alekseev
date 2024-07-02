package ru.otus.hw.dto.genre;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;

@Component
public class GenreMapper {

    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

}
