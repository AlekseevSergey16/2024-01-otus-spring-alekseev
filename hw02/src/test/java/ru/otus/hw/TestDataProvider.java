package ru.otus.hw;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

public class TestDataProvider {

    public List<Question> getListWithOneQuestion() {
        return List.of(
                new Question("What is the best programming language?", List.of(new Answer("Java", true))));
    }

    public List<Question> getTestQuestions() {
        return List.of(
                new Question("What is the best programming language?",
                        List.of(new Answer("Java", true), new Answer("C#", false), new Answer("C++", false))),
                new Question("How often are the Winter Olympic Games held?",
                        List.of(new Answer("Every year", false),
                                new Answer("Every 2 years", false),
                                new Answer("Every 4 years", true)))
        );
    }

}
