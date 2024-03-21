package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.TestDataProvider;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestBeanConfig;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = TestBeanConfig.class)
public class CsvQuestionDaoTest {

    private static final String TEST_QUESTIONS_FILE_NAME = "test-questions.csv";
    private static final String EMPTY_FILE_NAME = "empty-questions.csv";

    @Autowired
    private CsvQuestionDao sut;

    @MockBean
    private AppProperties appProperties;

    @Autowired
    private TestDataProvider testDataProvider;

    @Test
    void shouldFindAllQuestions() {
        //given
        given(appProperties.getTestFileName()).willReturn(TEST_QUESTIONS_FILE_NAME);
        List<Question> expectedQuestions = testDataProvider.createTestQuestions();

        //when
        List<Question> actualQuestions = sut.findAll();

        //then
        assertThat(actualQuestions).hasSize(2).containsAll(expectedQuestions);
    }

    @Test
    void shouldReturnEmptyListOfQuestions() {
        //given
        given(appProperties.getTestFileName()).willReturn(EMPTY_FILE_NAME);

        //when
        List<Question> questions = sut.findAll();

        //then
        assertThat(questions).isEmpty();
    }

    @Test
    void shouldFailureIfFileIsNotExists() {
        //given
        given(appProperties.getTestFileName()).willReturn("afsacsdc.csv");

        //when
        var result = assertThatThrownBy(sut::findAll);

        //then
        result.isInstanceOf(QuestionReadException.class);
    }

}
