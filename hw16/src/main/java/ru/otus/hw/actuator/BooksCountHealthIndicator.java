package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class BooksCountHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        try {
            long booksCount = bookRepository.count();
            if (booksCount > 0) {
                return Health.up()
                        .withDetail("msg", "Books count: " + booksCount)
                        .build();
            }
            return Health.down()
                    .withDetail("msg", "Books count <= 0: " + booksCount)
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage() == null ? "Couldn't get the number of books" : e.getMessage())
                    .build();
        }
    }
}
