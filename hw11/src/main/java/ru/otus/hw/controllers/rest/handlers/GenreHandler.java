package ru.otus.hw.controllers.rest.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.hw.services.GenreService;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class GenreHandler {

    private final GenreService genreService;

    public Mono<ServerResponse> getAllGenres(ServerRequest request) {
        return genreService.findAll().collectList()
                .flatMap(genres -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(genres)));
    }

}
