package ru.otus.hw.controllers.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    @MockBean
    private AuthorService authorService;

    @Test
    void shouldGetAllAuthors() throws Exception {
        List<AuthorDto> expectedAuthors = List.of(new AuthorDto(1L, "fullname1"),
                new AuthorDto(2L, "fullname2"));
        when(authorService.findAll()).thenReturn(expectedAuthors);

        MvcResult result = mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andReturn();

        List<AuthorDto> actualAuthors = objMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }

}
