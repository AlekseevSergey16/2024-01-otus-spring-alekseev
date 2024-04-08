package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private static final RowMapper<Author> AUTHOR_MAPPER = (rs, n) ->
            new Author(rs.getLong("id"), rs.getString("full_name"));

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<Author> findAll() {
        return jdbc.query("SELECT id, full_name FROM authors", AUTHOR_MAPPER);
    }

    @Override
    public Optional<Author> findById(long id) {
        final String sql = "SELECT id, full_name FROM authors WHERE id = :id";

        var params = new MapSqlParameterSource("id", id);

        return jdbc.query(sql, params, AUTHOR_MAPPER).stream().findAny();
    }
}
