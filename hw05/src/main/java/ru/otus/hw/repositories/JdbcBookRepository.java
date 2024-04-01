package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate jdbc;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        final String sql = """
                SELECT books.id, books.title,
                       books.author_id,
                       authors.full_name,
                       genres.id AS genre_id,
                       genres.name AS genre_name
                FROM books
                INNER JOIN authors ON authors.id = books.author_id
                LEFT JOIN books_genres ON books_genres.book_id = books.id
                LEFT JOIN genres ON genres.id = books_genres.genre_id
                WHERE books.id = :id
                """;

        var params = new MapSqlParameterSource("id", id);

        return Optional.ofNullable(jdbc.query(sql, params, JdbcBookRepository::bookResultSetExtractor));
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbc.getJdbcOperations().update("DELETE FROM books WHERE id = ?", id);
    }

    private List<Book> getAllBooksWithoutGenres() {
        final String sql = """
                SELECT books.id, books.title, books.author_id, authors.full_name
                FROM books
                INNER JOIN authors ON authors.id = books.author_id
                """;
        return jdbc.query(sql, JdbcBookRepository::bookMapper);
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("SELECT book_id, genre_id FROM books_genres",
                JdbcBookRepository::bookGenreRelationMapper);
    }

    private Map<Long, List<Genre>> getGenresByBookIdMap() {
        final String sql = """
                SELECT book_id, genre_id, genres.name AS genre_name
                FROM books_genres
                INNER JOIN genres ON genres.id = books_genres.genre_id
                """;
        return jdbc.query(sql, rs -> {
            Map<Long, List<Genre>> bookIdGenresMap = new HashMap<>();

            while (rs.next()) {
                long bookId = rs.getLong("book_id");
                List<Genre> genres = bookIdGenresMap.computeIfAbsent(bookId, k -> new ArrayList<>());
                genres.add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
            }

            return bookIdGenresMap;
        });
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)
        for (Book book : booksWithoutGenres) {
            for (BookGenreRelation bookGenreRelation : relations) {
                if (book.getId() == bookGenreRelation.bookId()) {
                    Genre genre = genres.stream()
                            .filter(g -> g.getId() == bookGenreRelation.genreId())
                            .findAny().orElseThrow();
                    book.getGenres().add(genre);
                }
            }
        }
    }

    private Book insert(Book book) {
        final String sql = """
                INSERT INTO books (title, author_id)
                VALUES (:title, :authorId)
                """;

        var params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId());

        var keyHolder = new GeneratedKeyHolder();

        jdbc.update(sql, params, keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private Book update(Book book) {
        final String sql = """
                UPDATE books
                SET title = :title,
                    author_id = :authorId
                WHERE id = :bookId
                """;

        var params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId())
                .addValue("bookId", book.getId());

        int numberOfUpdatedRows = jdbc.update(sql, params);
        if (numberOfUpdatedRows == 0) {
            throw new EntityNotFoundException("The book with id (%d) was not found".formatted(book.getId()));
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        final String sql = """
                INSERT INTO books_genres (book_id, genre_id)
                VALUES (:bookId, :genreId)
                """;
        final long bookId = book.getId();

        var params = book.getGenres().stream()
                .map(genre ->  new MapSqlParameterSource()
                        .addValue("bookId", bookId)
                        .addValue("genreId", genre.getId()))
                .toArray(MapSqlParameterSource[]::new);

        jdbc.batchUpdate(sql, params);
    }

    private void removeGenresRelationsFor(Book book) {
        final String sql = "DELETE FROM books_genres WHERE book_id = :bookId";

        var params = new MapSqlParameterSource("bookId", book.getId());

        jdbc.update(sql, params);
    }

    private static Book bookMapper(ResultSet rs, int n) throws SQLException {
        return new Book(rs.getLong("id"),
                rs.getString("title"),
                new Author(rs.getLong("author_id"), rs.getString("full_name")),
                new ArrayList<>());
    }

    // Использовать для findById
    private static Book bookResultSetExtractor(ResultSet rs) throws SQLException {
        Book book = null;
        List<Genre> genres = new ArrayList<>();
        while (rs.next()) {
            if (book == null) {
                book = new Book(rs.getLong("id"),
                        rs.getString("title"),
                        new Author(rs.getLong("author_id"), rs.getString("full_name")),
                        genres);
            }
            genres.add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
        }
        return book;
    }

    private static BookGenreRelation bookGenreRelationMapper(ResultSet rs, int n) throws SQLException {
        return new BookGenreRelation(rs.getLong("book_id"), rs.getLong("genre_id"));
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
