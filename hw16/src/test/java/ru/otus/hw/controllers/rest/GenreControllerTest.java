package ru.otus.hw.controllers.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    @MockBean
    private GenreService genreService;

    @Test
    void shouldGetAllGenres() throws Exception {
        List<GenreDto> expectedGenres = List.of(new GenreDto(1L, "genreName1"),
                new GenreDto(2L, "genreName2"));
        when(genreService.findAll()).thenReturn(expectedGenres);

        MvcResult result = mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andReturn();

        List<GenreDto> actualGenres = objMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }



}
