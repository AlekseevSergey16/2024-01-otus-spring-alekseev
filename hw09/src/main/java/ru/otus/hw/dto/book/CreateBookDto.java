package ru.otus.hw.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateBookDto {

    private Long id;

    private String title;

    private Long authorId;

    private Set<Long> genreIds;

}
