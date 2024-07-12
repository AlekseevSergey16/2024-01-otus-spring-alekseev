package ru.otus.hw.controllers.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.hw.controllers.rest.handlers.AuthorHandler;
import ru.otus.hw.controllers.rest.handlers.GenreHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class DictionaryRouting {

    @Bean
    public RouterFunction<ServerResponse> authorRouter(AuthorHandler authorHandler) {
        return route(GET("/api/authors"), authorHandler::getAllAuthors);
    }

    @Bean
    public RouterFunction<ServerResponse> genreRouter(GenreHandler genreHandler) {
        return route(GET("/api/genres"), genreHandler::getAllGenres);
    }

}
