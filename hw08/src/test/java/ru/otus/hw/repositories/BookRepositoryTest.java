package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class BookRepositoryTest {
    private static final String FIRST_AUTHOR_ID = "12";
    private static final String FIRST_GENRE_ID = "1";
    private static final String SECOND_GENRE_ID = "2";

    @Autowired
    private BookRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.save(new Author(FIRST_AUTHOR_ID, "Author fullName"));
        mongoTemplate.save(new Genre(FIRST_GENRE_ID, "genre1"));
        mongoTemplate.save(new Genre(SECOND_GENRE_ID, "genre2"));
    }

    @Test
    void shouldCreateNewBook() {
        //when
        Book expectedBook = repository.save(aBook().build());

        //then
        Book actualBook = mongoTemplate.findById(expectedBook.getId(), Book.class);
        assertBook(actualBook, expectedBook);
    }

    @Test
    void shouldUpdateBook() {
        //given
        Book book = mongoTemplate.insert(aBook()
                .author(mongoTemplate.insert(new Author(null, "aaa")))
                .genres(List.of(mongoTemplate.insert(new Genre(null, "genreName"))))
                .build());

        //when
        Book expectedBook = repository.save(aBook().id(book.getId()).build());

        //then
        Book actualBook = mongoTemplate.findById(expectedBook.getId(), Book.class);
        assertBook(actualBook, expectedBook);
    }

    @Test
    void shouldFindBookById() {
        //given
        Book expectedBook = mongoTemplate.insert(aBook().title("Interesting book").build());

        //when
        Optional<Book> actualBook = repository.findById(expectedBook.getId());

        //then
        assertThat(actualBook).isPresent();
        assertBook(actualBook.get(), expectedBook);
    }

    @Test
    void shouldDeleteBookWithComment() {
        //given
        Book expectedBook = mongoTemplate.insert(aBook().title("Interesting book").build());

        //when
        repository.deleteById(expectedBook.getId());

        //then
        assertThat(mongoTemplate.findById(expectedBook.getId(), Book.class)).isNull();
    }

    private static void assertBook(Book actualBook, Book expectedBook) {
        assertThat(actualBook.getId()).isEqualTo(expectedBook.getId());
        assertThat(actualBook.getTitle()).isEqualTo(expectedBook.getTitle());
        assertThat(actualBook.getAuthor().getId()).isEqualTo(expectedBook.getAuthor().getId());
        assertThat(actualBook.getGenres()).hasSize(expectedBook.getGenres().size());
    }

    private Book.BookBuilder aBook() {
        return Book.builder()
                .title("Book title")
                .author(new Author(FIRST_AUTHOR_ID, "Author fullName"))
                .genres(List.of(new Genre(FIRST_GENRE_ID, "genre1"), new Genre(SECOND_GENRE_ID, "genre2")));
    }

}
