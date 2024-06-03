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
import ru.otus.hw.dto.comment.CommentCreateDto;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CommentSummaryDto;
import ru.otus.hw.dto.comment.CommentUpdateDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    @MockBean
    private CommentService commentService;

    @Test
    void shouldCreateCommentAndReturn201StatusCode() throws Exception {
        CommentCreateDto commentDto = new CommentCreateDto(2L, "comment text");
        when(commentService.create(any())).thenReturn(null);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(commentDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400StatusCodeWhenCreateInvalidComment() throws Exception {
        CommentCreateDto commentDto = new CommentCreateDto(0L, " ");

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateCommentAndReturn200StatusCode() throws Exception {
        CommentUpdateDto commentDto = new CommentUpdateDto(2L, "comment text");

        mockMvc.perform(put("/api/comments/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400StatusCodeWhenUpdateInvalidComment() throws Exception {
        CommentUpdateDto commentDto = new CommentUpdateDto(null, "  ");

        mockMvc.perform(put("/api/comments/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetCommentById() throws Exception {
        CommentDto expectedComment = new CommentDto(2L, "text", 3L);
        when(commentService.findById(2L)).thenReturn(expectedComment);

        MvcResult result = mockMvc.perform(get("/api/comments/2"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(objMapper.readValue(result.getResponse().getContentAsString(), CommentDto.class))
                .isEqualTo(expectedComment);
    }

    @Test
    void shouldReturn404IfCommentIsNotExist() throws Exception {
        when(commentService.findById(2L)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/comments/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetCommentsByBookId() throws Exception {
        List<CommentSummaryDto> expectedComments = List.of(
                new CommentSummaryDto(1L, "text1"), new CommentSummaryDto(2L, "text2"));
        when(commentService.findByBookId(10L)).thenReturn(expectedComments);

        MvcResult result = mockMvc.perform(get("/api/comments").queryParam("bookId", "10"))
                .andExpect(status().isOk())
                .andReturn();

        List<CommentSummaryDto> actualComments = objMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
    }

    @Test
    void shouldDeleteCommentByIdAndReturn204StatusCode() throws Exception {
        doThrow(new RuntimeException()).when(commentService)
                .deleteById(ArgumentMatchers.longThat(arg -> !arg.equals(11L)));

        mockMvc.perform(delete("/api/comments/11"))
                .andExpect(status().isNoContent());
    }

}
