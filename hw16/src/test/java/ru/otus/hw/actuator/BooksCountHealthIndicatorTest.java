package ru.otus.hw.actuator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.repositories.BookRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BooksCountHealthIndicatorTest {

    @Autowired
    private BooksCountHealthIndicator booksCountHealthIndicator;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void shouldReturnHealthUpWhenCountIsPositive() {
        //given
        when(bookRepository.count()).thenReturn(10L);
        
        //when
        Health result = booksCountHealthIndicator.health();

        //then
        assertThat(result.getStatus()).isEqualTo(Status.UP);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -10})
    void shouldReturnHealthDownWhenCountIsNegativeOrZero(long count) {
        //given
        when(bookRepository.count()).thenReturn(count);

        //when
        Health result = booksCountHealthIndicator.health();

        //then
        assertThat(result.getStatus()).isEqualTo(Status.DOWN);
    }

    @Test
    void shouldReturnHealthDownWhenThrowEx() {
        //given
        when(bookRepository.count()).thenThrow(new RuntimeException());

        //when
        Health result = booksCountHealthIndicator.health();

        //then
        assertThat(result.getStatus()).isEqualTo(Status.DOWN);
    }

}
