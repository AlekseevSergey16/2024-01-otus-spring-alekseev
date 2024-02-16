package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import ru.otus.hw.TestDataProvider;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CsvQuestionDaoTest {

    private static final String TEST_QUESTIONS_FILE_NAME = "test-questions.csv";
    private static final String EMPTY_FILE_NAME = "empty-questions.csv";

    private final TestDataProvider testDataProvider = new TestDataProvider();

    @Test
    void shouldFindAllQuestions() {
        //given
        QuestionDao sut = new CsvQuestionDao(new AppProperties(3, TEST_QUESTIONS_FILE_NAME));
        List<Question> expectedQuestions = testDataProvider.getTestQuestions();

        //when
        List<Question> actualQuestions = sut.findAll();

        //then
        assertThat(actualQuestions).hasSize(2).containsAll(expectedQuestions);
    }

    @Test
    void shouldReturnEmptyListOfQuestions() {
        //given
        QuestionDao sut = new CsvQuestionDao(new AppProperties(3, EMPTY_FILE_NAME));

        //when
        List<Question> questions = sut.findAll();

        //then
        assertThat(questions).isEmpty();
    }

    @Test
    void shouldFailureIfFileIsNotExists() {
        //given
        QuestionDao sut = new CsvQuestionDao(new AppProperties(3, "afsacsdc.csv"));

        //when
        var result = assertThatThrownBy(sut::findAll);

        //then
        result.isInstanceOf(QuestionReadException.class);
    }

}
