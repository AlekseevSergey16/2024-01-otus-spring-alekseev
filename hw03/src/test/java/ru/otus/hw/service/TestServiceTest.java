package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.TestDataProvider;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class TestServiceTest {

    private final TestDataProvider testDataProvider = new TestDataProvider();

    private QuestionDao questionDao;
    private LocalizedIOService ioService;

    @BeforeEach
    void setUp() {
        questionDao = mock(QuestionDao.class);
        ioService = mock(LocalizedIOService.class);
    }

    @Test
    void shouldReturnSuccessfulTestResultForOneQuestion() {
        //given
        Student student = new Student("aaa", "bbb");
        Question question = testDataProvider.createQuestion();
        TestResult expectedTestResult = testDataProvider.createSuccessfulTestResult(student, question);

        given(questionDao.findAll()).willReturn(testDataProvider.createListWithOneQuestion(question));
        given(ioService.readIntForRangeLocalized(eq(1), eq(1), any())).willReturn(1);

        TestService sut = new TestServiceImpl(ioService, questionDao);

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

        TestService sut = new TestServiceImpl(ioService, questionDao);

        //when
        TestResult actualTestResult = sut.executeTestFor(student);

        //then
        assertThat(actualTestResult).usingRecursiveComparison().isEqualTo(expectedTestResult);
    }

}
