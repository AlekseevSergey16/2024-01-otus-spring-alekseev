package ru.otus.hw.controllers.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.dto.book.BookCreateDto;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.BookSummaryDto;
import ru.otus.hw.dto.book.BookUpdateDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    @MockBean
    private BookService bookService;

    @Test
    void shouldCreateNewBookAndReturn201StatusCode() throws Exception {
        BookCreateDto bookCreateDto = new BookCreateDto("book title", 1L, Set.of(1L, 2L));
        when(bookService.create(any())).thenReturn(null);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(bookCreateDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400StatusCodeWhenCreateInvalidBook() throws Exception {
        BookCreateDto bookCreateDto = new BookCreateDto("book title", 1L, new HashSet<>(Arrays.asList(null, 2L)));
        when(bookService.create(any())).thenReturn(null);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(bookCreateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateBookAndReturn200StatusCode() throws Exception {
        BookUpdateDto bookUpdateDto = new BookUpdateDto(10L, "book title", 2L, Set.of(1L, 2L));
        when(bookService.update(any())).thenReturn(null);

        mockMvc.perform(put("/api/books/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(bookUpdateDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400StatusCodeWhenUpdateInvalidBook() throws Exception {
        BookUpdateDto bookUpdateDto = new BookUpdateDto(null, "title", 2L, new HashSet<>(Arrays.asList(null, 2L)));
        when(bookService.update(any())).thenReturn(null);

        mockMvc.perform(put("/api/books/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(bookUpdateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllBooks() throws Exception {
        List<BookDto> books = List.of(new BookDto(1, "title",
                new AuthorDto(1, "full name"),
                List.of(new GenreDto(1, "genre1"), new GenreDto(2, "genre2"))));
        when(bookService.findAll()).thenReturn(books);

        MvcResult result = mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(objMapper
                .readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<BookDto>>() {}))
                .hasSize(1).containsExactly(books.toArray(BookDto[]::new));
    }

    @Test
    void shouldGetBookById() throws Exception {
        BookSummaryDto bookDto = new BookSummaryDto(10, "title", 1L, Set.of(1L, 2L));
        when(bookService.findById(10)).thenReturn(bookDto);

        MvcResult result = mockMvc.perform(get("/api/books/10"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(objMapper.readValue(result.getResponse().getContentAsString(), BookSummaryDto.class))
                .isEqualTo(bookDto);
    }

    @Test
    void shouldReturn404IfBookIsNotExist() throws Exception {
        when(bookService.findById(11)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/books/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteBookByIdAndReturn204StatusCode() throws Exception {
        doThrow(new RuntimeException()).when(bookService).deleteById(ArgumentMatchers.longThat(arg -> !arg.equals(11L)));

        mockMvc.perform(delete("/api/books/11"))
                .andExpect(status().isNoContent());
    }
}
