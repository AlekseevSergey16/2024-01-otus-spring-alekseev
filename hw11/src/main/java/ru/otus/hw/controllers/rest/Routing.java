package ru.otus.hw.controllers.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Routing {

    @Bean
    public RouterFunction<ServerResponse> authorRouter(AuthorHandler authorHandler) {
        return route(GET("/api/authors"), authorHandler::getAllAuthors);
    }

    @Bean
    public RouterFunction<ServerResponse> genreRouter(GenreHandler genreHandler) {
        return route(GET("/api/genres"), genreHandler::getAllGenres);
    }

    @Bean
    public RouterFunction<ServerResponse> bookRouter(BookHandler bookHandler) {
        return route()
                .POST("/api/books", bookHandler::createBook)
                .PUT("/api/books/{id}", bookHandler::updateBook)
                .GET("/api/books", bookHandler::getAllBooks)
                .GET("/api/books/{id}", bookHandler::getBookById)
                .DELETE("/api/books/{id}", bookHandler::deleteBook)
                .build();
    }

}
