package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            printQuestion(question);

            Answer answer = getStudentAnswer(question.answers());
            ioService.printLine("");

            testResult.applyAnswer(question, answer.isCorrect());
        }

        return testResult;
    }

    private void printQuestion(Question question) {
        ioService.printLine(question.text());
        printAnswers(question.answers());
        ioService.printLine("");
    }

    private void printAnswers(List<Answer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            ioService.printFormattedLine("%d) %s", i + 1, answers.get(i).text());
        }
    }

    private Answer getStudentAnswer(List<Answer> answers) {
        int answerIdx = ioService.readIntForRange(1, answers.size(), "There is no such answer\n");
        return answers.get(answerIdx - 1);
    }
}
