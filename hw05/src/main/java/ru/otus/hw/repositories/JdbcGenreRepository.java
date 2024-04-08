package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class JdbcGenreRepository implements GenreRepository {

    private static final RowMapper<Genre> GENRE_MAPPER = (rs, n) -> new Genre(rs.getLong("id"), rs.getString("name"));

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<Genre> findAll() {
        return jdbc.query("SELECT id, name FROM genres", GENRE_MAPPER);
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        final String sql = "SELECT id, name FROM genres WHERE id IN(:ids)";

        var params = new MapSqlParameterSource("ids", ids);

        return jdbc.query(sql, params, GENRE_MAPPER);
    }
}
