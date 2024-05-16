package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        var book = em.find(Book.class, id);
        if (book != null) {
            em.remove(em.find(Book.class, id));
        }
    }

    @Override
    public Optional<Book> findById(long id) {
        return em.createQuery("from Book where id = :id", Book.class)
                .setHint("jakarta.persistence.fetchgraph", em.getEntityGraph("book-with-author-and-genres-graph"))
                .setParameter("id", id)
                .getResultList().stream().findAny();
    }

    @Override
    public List<Book> findAll() {
        return em.createQuery("from Book", Book.class)
                .setHint("jakarta.persistence.fetchgraph", em.getEntityGraph("book-with-author-graph"))
                .getResultList();
    }

}
