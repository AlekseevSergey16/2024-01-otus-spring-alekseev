package ru.otus.hw.dto.book;

import java.util.Set;

public record BookSummaryDto(long id, String title, Long authorId, Set<Long> genreIds) {
}
