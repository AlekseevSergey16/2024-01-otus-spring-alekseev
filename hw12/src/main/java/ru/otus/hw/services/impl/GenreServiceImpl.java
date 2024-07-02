package ru.otus.hw.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.genre.GenreMapper;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(genreMapper::toDto)
                .toList();
    }
}
