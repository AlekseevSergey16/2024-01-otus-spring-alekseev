package ru.otus.hw.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class BookCreateDto {

    private Long id;

    @NotBlank(message = "title should not be blank")
    private String title;

    @NotNull(message = "authorId should not be empty")
    private Long authorId;

    @NotEmpty
    private Set<Long> genreIds;

}
