package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldCreateNewComment() {
        //given
        Comment expectedComment = aComment().text("comment 045").build();

        //when
        Comment savedComment = repository.save(expectedComment);
        em.flush();

        //then
        Comment actualComment = em.find(Comment.class, savedComment.getId());
        assertThat(actualComment).isNotNull();
        assertThat(actualComment.getText()).isEqualTo(expectedComment.getText());
        assertThat(actualComment.getBook().getId()).isEqualTo(expectedComment.getBook().getId());
    }

    @Test
    void shouldUpdateCommentFromDb() {
        //given
        Comment comment = em.find(Comment.class, 1);
        comment.setText("text_test");
        comment.setBook(em.getEntityManager().getReference(Book.class, 3));;
        em.detach(comment);

        //when
        Comment updatedComment = repository.save(comment);
        em.flush();

        //then
        Comment actualComment = em.find(Comment.class, updatedComment.getId());
        assertThat(actualComment).isNotNull();
        assertThat(actualComment.getText()).isEqualTo(comment.getText());
        assertThat(actualComment.getBook().getId()).isEqualTo(3);
    }

    @Test
    void shouldUpdateComment() {
        //given
        Comment comment = aComment().text("new comment text").build();
        em.persistAndFlush(comment);

        //when
        comment.setText("updated text");
        comment.setBook(em.getEntityManager().getReference(Book.class, 2));
        Comment updatedComment = repository.save(comment);
        em.flush();

        //then
        em.detach(comment);
        Comment actualComment = em.find(Comment.class, updatedComment.getId());
        assertThat(actualComment).isNotNull();
        assertThat(actualComment.getText()).isEqualTo(comment.getText());
        assertThat(actualComment.getBook().getId()).isEqualTo(2);
    }

    @Test
    void shouldFindCommentById() {
        //given
        Comment expectedComment = aComment().build();
        em.persistAndFlush(expectedComment);

        //when
        Optional<Comment> actualComment = repository.findById(expectedComment.getId());

        //then
        assertThat(actualComment).isPresent();
        assertThat(actualComment.get().getId()).isEqualTo(expectedComment.getId());
        assertThat(actualComment.get().getText()).isEqualTo(expectedComment.getText());
        assertThat(actualComment.get().getBook().getId()).isEqualTo(expectedComment.getBook().getId());
    }

    @Test
    void shouldReturnCorrectCommentsByBookId() {
        //given
        Book book = em.getEntityManager().getReference(Book.class, 2);
        List<Comment> expectedComments = List.of(
                aComment().text("comment text1").book(book).build(),
                aComment().text("comment text2").book(book).build());
        saveComments(expectedComments);

        //when
        List<Comment> actualComments = repository.findByBookId(book.getId());

        //then
        assertThat(actualComments).isNotNull().hasSize(expectedComments.size())
                .anyMatch(comment -> comment.getId() == expectedComments.get(0).getId())
                .anyMatch(comment -> comment.getText().equals(expectedComments.get(0).getText()))
                .anyMatch(comment -> comment.getId() == expectedComments.get(1).getId())
                .anyMatch(comment -> comment.getText().equals(expectedComments.get(1).getText()));
    }

    private Comment.CommentBuilder aComment() {
        return Comment.builder()
                .id(0)
                .text("comment")
                .book(em.getEntityManager().getReference(Book.class, 3));
    }

    private void saveComments(List<Comment> comments) {
        comments.forEach(comment -> em.persist(comment));
        em.flush();
    }

}
