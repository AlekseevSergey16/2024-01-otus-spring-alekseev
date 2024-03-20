package ru.otus.hw.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.otus.hw.TestDataProvider;

@TestConfiguration
public class TestBeanConfig {

    @Bean
    public TestDataProvider testDataProvider() {
        return new TestDataProvider();
    }

}
