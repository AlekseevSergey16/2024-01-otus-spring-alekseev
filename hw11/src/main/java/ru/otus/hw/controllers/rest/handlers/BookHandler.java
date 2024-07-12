package ru.otus.hw.controllers.rest.handlers;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.book.BookCreateDto;
import ru.otus.hw.dto.book.BookUpdateDto;
import ru.otus.hw.services.BookService;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class BookHandler {

    private final BookService bookService;

    private final Validator validator;

    public Mono<ServerResponse> createBook(ServerRequest request) {
        return request.bodyToMono(BookCreateDto.class)
                .doOnNext(this::validate)
                .flatMap(bookService::create)
                .flatMap(book -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(book)))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> updateBook(ServerRequest request) {
        return request.bodyToMono(BookUpdateDto.class)
                .doOnNext(this::validate)
                .flatMap(bookService::update)
                .flatMap(book -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(book)))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> getAllBooks(ServerRequest request) {
        return bookService.findAll().collectList()
                .flatMap(books -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(books)));
    }

    public Mono<ServerResponse> getBookById(ServerRequest request) {
        return bookService.findById(request.pathVariable("id"))
                .flatMap(books -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(books)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteBook(ServerRequest request) {
        return bookService.deleteById(request.pathVariable("id"))
                .then(ServerResponse.noContent().build());
    }

    private <T> void validate(T object) {
        var errors = validator.validate(object);
        if (!errors.isEmpty()) {
            throw new ServerWebInputException(errors.toString());
        }
    }

}
