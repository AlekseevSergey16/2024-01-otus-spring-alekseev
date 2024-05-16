package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaGenreRepository.class)
public class JpaGenreRepositoryTest {
    private static final int GENRE_COUNT = 6;

    @Autowired
    private JpaGenreRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnCorrectGenresByIds() {
        //given
        List<Genre> expectedGenres = List.of(em.find(Genre.class, 1), em.find(Genre.class, 2), em.find(Genre.class, 3));
        em.clear();

        //when
        List<Genre> actualGenres = repository.findAllByIds(Set.of(1L, 2L, 3L));

        //then
        assertThat(actualGenres)
                .usingRecursiveComparison()
                .isEqualTo(expectedGenres);
    }

    @Test
    void shouldReturnCorrectGenresList() {
        //when
        List<Genre> genres = repository.findAll();

        //then
        assertThat(genres).hasSize(GENRE_COUNT);
    }

}
