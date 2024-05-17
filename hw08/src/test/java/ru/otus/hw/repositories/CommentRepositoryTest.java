package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class CommentRepositoryTest {
    private static final String FIRST_AUTHOR_ID = "12";
    private static final String FIRST_GENRE_ID = "1";
    private static final String SECOND_GENRE_ID = "2";
    private static final String FIRST_BOOK_ID = "123";

    @Autowired
    private CommentRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        Author author = mongoTemplate.save(new Author(FIRST_AUTHOR_ID, "Author fullName"));
        List<Genre> genres = new ArrayList<>();
        genres.add(mongoTemplate.save(new Genre(FIRST_GENRE_ID, "genre1")));
        genres.add(mongoTemplate.save(new Genre(SECOND_GENRE_ID, "genre2")));
        mongoTemplate.save(new Book(FIRST_BOOK_ID, "book title", author, genres));
    }

    @Test
    void shouldCreateNewComment() {
        //given
        Comment expectedComment = aComment()
                .text("comment 045")
                .book(Book.builder().id(FIRST_BOOK_ID).build())
                .build();

        //when
        Comment savedComment = repository.save(expectedComment);

        //then
        Comment actualComment = mongoTemplate.findById(savedComment.getId(), Comment.class);
        assertThat(actualComment).isNotNull();
        assertThat(actualComment.getText()).isEqualTo(expectedComment.getText());
        assertThat(actualComment.getBook().getId()).isEqualTo(expectedComment.getBook().getId());
    }

    @Test
    void shouldUpdateComment() {
        //given
        Comment comment = mongoTemplate.save(aComment().text("new comment text").build());

        //when
        Comment updatedComment = aComment().id(comment.getId())
                .text("updated comment")
                .book(Book.builder().id(FIRST_BOOK_ID).build())
                .build();
        repository.save(updatedComment);

        //then
        Comment actualComment = mongoTemplate.findById(updatedComment.getId(), Comment.class);
        assertThat(actualComment).isNotNull();
        assertThat(actualComment.getText()).isEqualTo(updatedComment.getText());
        assertThat(actualComment.getBook().getId()).isEqualTo(FIRST_BOOK_ID);
    }

    @Test
    void shouldReturnCorrectCommentsByBookId() {
        //given
        repository.deleteAll();
        Book book = Book.builder().id(FIRST_BOOK_ID).build();
        List<Comment> expectedComments = List.of(
                mongoTemplate.insert(aComment().text("comment text1").book(book).build()),
                mongoTemplate.insert(aComment().text("comment text2").book(book).build()));

        //when
        List<Comment> actualComments = repository.findByBookId(book.getId());

        //then
        assertThat(actualComments).isNotNull().hasSize(expectedComments.size())
                .anyMatch(comment -> Objects.equals(comment.getId(), expectedComments.get(0).getId()))
                .anyMatch(comment -> comment.getText().equals(expectedComments.get(0).getText()))
                .anyMatch(comment -> Objects.equals(comment.getId(), expectedComments.get(1).getId()))
                .anyMatch(comment -> comment.getText().equals(expectedComments.get(1).getText()));
    }

    private Comment.CommentBuilder aComment() {
        return Comment.builder()
                .text("comment");
    }

}
