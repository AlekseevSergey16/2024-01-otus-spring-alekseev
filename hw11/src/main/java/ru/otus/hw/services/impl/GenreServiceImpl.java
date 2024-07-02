package ru.otus.hw.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.genre.GenreMapper;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.GenreService;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    public Flux<GenreDto> findAll() {
        return genreRepository.findAll().map(genreMapper::toDto);
    }
}
