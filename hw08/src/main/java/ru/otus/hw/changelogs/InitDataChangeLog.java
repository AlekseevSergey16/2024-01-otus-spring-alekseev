package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@ChangeLog
public class InitDataChangeLog {
    private static final String SALEKSEEV = "salekseev";

    private final List<Author> authors = new ArrayList<>(3);

    private final List<Genre> genres = new ArrayList<>(6);

    private final List<Book> books = new ArrayList<>(3);

    @ChangeSet(order = "000", id = "dropDB", author = SALEKSEEV, runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = SALEKSEEV, runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        for (int i = 0; i < 3; i++) {
            var author = repository.save(new Author(null, "author" + i));
            authors.add(author);
        }
    }

    @ChangeSet(order = "002", id = "initGenres", author = SALEKSEEV, runAlways = true)
    public void initGenres(GenreRepository repository) {
        for (int i = 0; i < 6; i++) {
            Genre genre = repository.save(new Genre(null, "genre" + i));
            genres.add(genre);
        }
    }

    @ChangeSet(order = "003", id = "initBooks", author = SALEKSEEV, runAlways = true)
    public void initBooks(BookRepository repository) {
        for (int i = 0; i < 3; i++) {
            Book book = repository.save(new Book(null, "book" + i, authors.get(i), genres.subList(i, i + 2)));
            books.add(book);
        }
    }

    @ChangeSet(order = "004", id = "initComments", author = SALEKSEEV, runAlways = true)
    public void initComments(CommentRepository repository) {
        for (int i = 0; i < 3; i++) {
            repository.save(new Comment(null, "comment" + i, books.get(0)));
        }
    }

}
