package ru.otus.hw.controllers.rest.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.hw.services.AuthorService;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class AuthorHandler {

    private final AuthorService authorService;

    public Mono<ServerResponse> getAllAuthors(ServerRequest request) {
        return authorService.findAll().collectList()
                .flatMap(authors ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(authors)));
    }

}
