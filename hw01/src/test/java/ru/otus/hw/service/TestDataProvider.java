package ru.otus.hw.service;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

class TestDataProvider {

    List<Question> getListWithOneQuestion() {
        return List.of(
                new Question("What is the best programming language?", List.of(new Answer("Java", true))));
    }

}
