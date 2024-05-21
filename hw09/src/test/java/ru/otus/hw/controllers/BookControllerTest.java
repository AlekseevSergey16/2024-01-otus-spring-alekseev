package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    void shouldReturnCorrectBookListPage() throws Exception {
        //given
        List<BookDto> books = List.of(new BookDto(1, "title",
                new AuthorDto(1, "full name"),
                List.of(new GenreDto(1, "genre1"), new GenreDto(2, "genre2"))));
        when(bookService.findAll()).thenReturn(books);

        //when then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list-page"))
                .andExpect(model().attribute("books", books));
    }

    @Test
    void shouldReturnCorrectCreateBookPage() throws Exception {
        //given
        List<AuthorDto> authors = List.of(new AuthorDto(1, "full name"), new AuthorDto(2, "full name2"));
        List<GenreDto> genres = List.of(new GenreDto(1, "genre1"), new GenreDto(2, "genre2"));
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);

        //when then
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-form"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void shouldReturnCorrectEditBookPage() throws Exception {
        //given
        List<AuthorDto> authors = List.of(new AuthorDto(1, "full name"), new AuthorDto(2, "full name2"));
        List<GenreDto> genres = List.of(new GenreDto(1, "genre1"), new GenreDto(2, "genre2"));
        BookDto book = new BookDto(10, "title", authors.get(0), genres);
        when(bookService.findById(10)).thenReturn(book);
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);

        //when then
        mockMvc.perform(get("/books/10"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-form"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void shouldReturn404WhenEditPageIfBookNotFound() throws Exception {
        //given
        when(bookService.findById(anyLong())).thenThrow(EntityNotFoundException.class);

        //when then
        mockMvc.perform(get("/books/10")).andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateBook() throws Exception {
        //given
        when(bookService.create(any())).thenReturn(null);

        //when then
        mockMvc.perform(post("/books")
                .param("title", "book title")
                .param("authorId", "2")
                .param("genreIds", "1,2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void shouldBadRequestWhenCreateInvalidBook() throws Exception {
        //given
        when(bookService.create(any())).thenReturn(null);

        //when then
        mockMvc.perform(post("/books")
                        .param("title", "  ")
                        .param("authorId", "")
                        .param("genreIds", "1,2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldEditBook() throws Exception {
        //given
        List<AuthorDto> authors = List.of(new AuthorDto(1, "full name"), new AuthorDto(2, "full name2"));
        List<GenreDto> genres = List.of(new GenreDto(1, "genre1"), new GenreDto(2, "genre2"));
        BookDto book = new BookDto(10, "title", authors.get(0), genres);
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);
        when(bookService.findById(10)).thenReturn(book);
        when(bookService.update(eq(10), any())).thenReturn(null);

        //when then
        mockMvc.perform(post("/books/10")
                        .param("title", "book title")
                        .param("authorId", "2")
                        .param("genreIds", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        //given
        doNothing().when(bookService).deleteById(11);

        //when then
        mockMvc.perform(post("/books/11/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

}
