package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.BookSummaryDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
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
        mockMvc.perform(get("/").with(aUser()))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list-page"))
                .andExpect(model().attribute("books", books));
    }

    @Test
    void shouldReturn401StatusCodeForBookListPage() throws Exception {
        //when then
        mockMvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnCorrectCreateBookPage() throws Exception {
        //given
        List<AuthorDto> authors = List.of(new AuthorDto(1, "full name"), new AuthorDto(2, "full name2"));
        List<GenreDto> genres = List.of(new GenreDto(1, "genre1"), new GenreDto(2, "genre2"));
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);

        //when then
        mockMvc.perform(get("/books").with(aUser("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("book-form"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void shouldReturn401StatusForCreateBookPage() throws Exception {
        //when then
        mockMvc.perform(get("/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn403StatusForCreateBookPage() throws Exception {
        //when then
        mockMvc.perform(get("/books").with(aUser("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnCorrectEditBookPage() throws Exception {
        //given
        List<AuthorDto> authors = List.of(new AuthorDto(1, "full name"), new AuthorDto(2, "full name2"));
        List<GenreDto> genres = List.of(new GenreDto(1, "genre1"), new GenreDto(2, "genre2"));
        BookSummaryDto book = new BookSummaryDto(10L, "title", authors.get(0).id(), genres.stream()
                .map(GenreDto::id).collect(Collectors.toSet()));
        when(bookService.findById(10)).thenReturn(book);
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);

        //when then
        mockMvc.perform(get("/books/10").with(aUser("ADMIN")))
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
        mockMvc.perform(get("/books/10").with(aUser("ADMIN"))).andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateBook() throws Exception {
        //given
        when(bookService.create(any())).thenReturn(null);

        //when then
        mockMvc.perform(post("/books").with(csrf()).with(aUser("ADMIN"))
                    .param("title", "book title")
                    .param("authorId", "2")
                    .param("genreIds", "1,2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void shouldReturn401WhenCreateBook() throws Exception {
        //given
        when(bookService.create(any())).thenReturn(null);

        //when then
        mockMvc.perform(post("/books").with(csrf())
                        .param("title", "book title")
                        .param("authorId", "2")
                        .param("genreIds", "1,2"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenCreateBook() throws Exception {
        //when then
        mockMvc.perform(post("/books").with(csrf())
                        .param("title", "book title")
                        .param("authorId", "2")
                        .param("genreIds", "1,2"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldBadRequestWhenCreateInvalidBook() throws Exception {
        //given
        when(bookService.create(any())).thenReturn(null);

        //when then
        mockMvc.perform(post("/books").with(csrf()).with(aUser("ADMIN"))
                        .param("title", "  ")
                        .param("authorId", "")
                        .param("genreIds", "1,2"))
                .andExpect(model().hasErrors())
                .andExpect(status().isOk());
    }

    @Test
    void shouldBadRequestWhenUpdateInvalidBook() throws Exception {
        //given
        when(bookService.update(any())).thenReturn(null);

        //when then
        mockMvc.perform(post("/books/1").with(csrf()).with(aUser("ADMIN"))
                        .param("id", "1")
                        .param("title", "title")
                        .param("authorId", "1")
                        .param("genreIds", ""))
                .andExpect(model().hasErrors())
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateBook() throws Exception {
        //given
        List<AuthorDto> authors = List.of(new AuthorDto(1, "full name"), new AuthorDto(2, "full name2"));
        List<GenreDto> genres = List.of(new GenreDto(1, "genre1"), new GenreDto(2, "genre2"));
        BookSummaryDto book = new BookSummaryDto(10L, "title", 1L, genres.stream()
                .map(GenreDto::id).collect(Collectors.toSet()));
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);
        when(bookService.findById(10)).thenReturn(book);
        when(bookService.update(any())).thenReturn(null);

        //when then
        mockMvc.perform(post("/books/10").with(csrf()).with(aUser("ADMIN"))
                        .param("title", "book title")
                        .param("authorId", "2")
                        .param("genreIds", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/books/10"));
    }

    @Test
    void shouldReturn401StatusWhenUpdateBook() throws Exception {
        //when then
        mockMvc.perform(post("/books/10").with(csrf())
                        .param("title", "book title")
                        .param("authorId", "2")
                        .param("genreIds", "1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn403StatusWhenUpdateBook() throws Exception {
        //when then
        mockMvc.perform(post("/books/10").with(csrf()).with(aUser("USER"))
                        .param("title", "book title")
                        .param("authorId", "2")
                        .param("genreIds", "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldDeleteBook() throws Exception {
        //given
        doNothing().when(bookService).deleteById(11);

        //when then
        mockMvc.perform(post("/books/11/delete").with(csrf()).with(aUser("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void shouldReturn401WhenDeleteBook() throws Exception {
        //when then
        mockMvc.perform(post("/books/11/delete").with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn403WhenDeleteBook() throws Exception {
        //when then
        mockMvc.perform(post("/books/11/delete").with(csrf()).with(aUser("USER")))
                .andExpect(status().isForbidden());
    }

    public static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor aUser() {
        return user("user").password("password");
    }

    public static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor aUser(String authority) {
        return user("user").password("password").authorities(new SimpleGrantedAuthority(authority));
    }

}
