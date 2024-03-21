package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.TestDataProvider;
import ru.otus.hw.config.TestBeanConfig;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TestBeanConfig.class)
public class TestServiceTest {

    @Autowired
    private TestService sut;

    @Autowired
    private TestDataProvider testDataProvider;

    @MockBean
    private QuestionDao questionDao;

    @MockBean
    private LocalizedIOService ioService;

    @Test
    void shouldReturnSuccessfulTestResultForOneQuestion() {
        //given
        Student student = new Student("aaa", "bbb");
        Question question = testDataProvider.createQuestion();
        TestResult expectedTestResult = testDataProvider.createSuccessfulTestResult(student, question);

        given(questionDao.findAll()).willReturn(testDataProvider.createListWithOneQuestion(question));
        given(ioService.readIntForRangeLocalized(eq(1), eq(1), any())).willReturn(1);

        //when
        TestResult actualTestResult = sut.executeTestFor(student);

        //then
        assertThat(actualTestResult).usingRecursiveComparison().isEqualTo(expectedTestResult);
    }

    @Test
    void shouldReturnSuccessfulTestResult() {
        Student student = new Student("ccc", "ddd");
        List<Question> questions = testDataProvider.createTestQuestions();
        TestResult expectedTestResult = testDataProvider.createTestResultWithOneRightAnswer(student, questions);

        given(questionDao.findAll()).willReturn(questions);
        given(ioService.readIntForRangeLocalized(anyInt(), anyInt(), any())).willReturn(3);

        //when
        TestResult actualTestResult = sut.executeTestFor(student);

        //then
        assertThat(actualTestResult).usingRecursiveComparison().isEqualTo(expectedTestResult);
    }

}
