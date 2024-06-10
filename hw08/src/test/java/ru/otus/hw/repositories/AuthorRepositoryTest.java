package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void shouldFindExpectedAuthorById() {
        //given
        Author expectedAuthor = mongoTemplate.insert(new Author(null, "Author fullName"));

        //when
        Optional<Author> actualAuthor = repository.findById(expectedAuthor.getId());

        //then
        assertThat(actualAuthor).isPresent()
                .get().usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

}
