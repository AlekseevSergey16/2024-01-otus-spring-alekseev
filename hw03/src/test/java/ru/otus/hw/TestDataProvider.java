package ru.otus.hw;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

public class TestDataProvider {

    public List<Question> createListWithOneQuestion(Question question) {
        return List.of(question);
    }

    public List<Question> createTestQuestions() {
        return List.of(
                new Question("What is the best programming language?",
                        List.of(new Answer("Java", true), new Answer("C#", false), new Answer("C++", false))),
                new Question("How often are the Winter Olympic Games held?",
                        List.of(new Answer("Every year", false),
                                new Answer("Every 2 years", false),
                                new Answer("Every 4 years", true)))
        );
    }

    public Question createQuestion() {
        return new Question("What is the best programming language?", List.of(new Answer("Java", true)));
    }

    public TestResult createSuccessfulTestResult(Student student, Question question) {
        TestResult testResult = new TestResult(student);
        testResult.getAnsweredQuestions().add(question);
        testResult.setRightAnswersCount(1);
        return testResult;
    }

    public TestResult createTestResultWithOneRightAnswer(Student student, List<Question> questions) {
        TestResult testResult = new TestResult(student);
        testResult.getAnsweredQuestions().addAll(questions);
        testResult.setRightAnswersCount(1);
        return testResult;
    }

}
