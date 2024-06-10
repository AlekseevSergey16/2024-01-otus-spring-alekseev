package ru.otus.hw.repositories;

import org.bson.BsonDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.getCollection("genres").deleteMany(new BsonDocument());
    }

    @Test
    void shouldReturnCorrectGenresByIds() {
        //given
        List<Genre> expectedGenres = new ArrayList<>();
        expectedGenres.add(mongoTemplate.insert(new Genre(null, "genre name1")));
        expectedGenres.add(mongoTemplate.insert(new Genre(null, "genre name2")));

        //when
        List<Genre> actualGenres = repository.findAllById(
                List.of(expectedGenres.get(0).getId(), expectedGenres.get(1).getId()));

        //then
        assertThat(actualGenres)
                .usingRecursiveComparison()
                .isEqualTo(expectedGenres);
    }

    @Test
    void shouldReturnCorrectGenresList() {
        //given
        List<Genre> expectedGenres = new ArrayList<>();
        expectedGenres.add(mongoTemplate.insert(new Genre(null, "genre name1")));
        expectedGenres.add(mongoTemplate.insert(new Genre(null, "genre name2")));

        //when
        List<Genre> actualGenres = repository.findAll();

        //then
        assertThat(actualGenres)
                .usingRecursiveComparison()
                .isEqualTo(expectedGenres);
    }

}
