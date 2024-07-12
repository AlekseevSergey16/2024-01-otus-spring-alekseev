package ru.otus.hw.controllers.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.controllers.rest.handlers.BookHandler;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.dto.book.BookCreateDto;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.BookSummaryDto;
import ru.otus.hw.dto.book.BookUpdateDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.services.BookService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = {BookRouting.class, BookHandler.class})
public class BookRouterTest {

    @Autowired
    private RouterFunction<ServerResponse> bookRouter;

    @MockBean
    private BookService bookService;

    private WebTestClient client;

    @BeforeEach
    void setUp() {
        this.client = WebTestClient
                .bindToRouterFunction(bookRouter)
                .build();
    }

    @Test
    void shouldCreateNewBookAndReturn200StatusCode() {
        BookCreateDto bookCreateDto = new BookCreateDto("book title", "1", Set.of("1", "2"));
        when(bookService.create(any())).thenReturn(Mono.just(new BookDto(
                "id", "book title", new AuthorDto("1", "name"), List.of())));

        client.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bookCreateDto))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturn400StatusCodeWhenCreateInvalidBook() {
        BookCreateDto bookCreateDto = new BookCreateDto("book title", "1", new HashSet<>(Arrays.asList(null, "1")));

        client.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bookCreateDto))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldUpdateBookAndReturn200StatusCode() {
        BookUpdateDto bookUpdateDto = new BookUpdateDto("10", "book title", "2", Set.of("1", "2"));
        when(bookService.update(any())).thenReturn(Mono.just(new BookDto(
                "id", "book title", new AuthorDto("1", "name"), List.of())));

        client.put()
                .uri("/api/books/10")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bookUpdateDto))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturn400StatusCodeWhenUpdateInvalidBook() {
        BookUpdateDto bookUpdateDto = new BookUpdateDto(null, "title", "2", new HashSet<>(Arrays.asList(null, "2")));

        client.put()
                .uri("/api/books/11")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bookUpdateDto))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldGetAllBooks() {
        BookDto[] books = new BookDto[] {new BookDto("1", "title",
                new AuthorDto("1", "full name"),
                List.of(new GenreDto("1", "genre1"), new GenreDto("2", "genre2")))};
        when(bookService.findAll()).thenReturn(Flux.fromArray(books));


        List<BookDto> result = client.get()
                .uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody()
                .collectList()
                .block();

        assertThat(result)
                .hasSize(1).containsExactly(books);
    }

    @Test
    void shouldGetBookById() {
        BookSummaryDto bookDto = new BookSummaryDto("10", "title", "1", Set.of("1", "2"));
        when(bookService.findById("10")).thenReturn(Mono.just(bookDto));

        BookSummaryDto result = client.get()
                .uri("/api/books/10")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookSummaryDto.class)
                .getResponseBody()
                .blockFirst();

        assertThat(result).isEqualTo(bookDto);
    }

    @Test
    void shouldDeleteBookByIdAndReturn204StatusCode() {
        when(bookService.deleteById(any())).thenReturn(Mono.empty());

        client.delete()
                .uri("/api/books/10")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

}
