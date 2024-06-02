package ru.otus.hw.dto.comment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentCreateDto {

    @Min(1)
    @NotNull
    private Long bookId;

    @NotBlank
    private String text;

}
