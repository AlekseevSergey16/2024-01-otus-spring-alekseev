package ru.otus.hw.dto.book;

import java.util.Set;

public record BookSummaryDto(String id, String title, String authorId, Set<String> genreIds) {
}
