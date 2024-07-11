package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
