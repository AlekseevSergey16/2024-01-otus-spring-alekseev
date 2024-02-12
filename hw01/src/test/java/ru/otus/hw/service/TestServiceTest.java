package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;

import static org.mockito.Mockito.*;

class TestServiceTest {

    private final TestDataProvider testDataProvider = new TestDataProvider();

    private QuestionDao questionDao;
    private IOService ioService;

    @BeforeEach
    void setUp() {
        questionDao = mock(QuestionDao.class);
        ioService = mock(IOService.class);
    }

    @Test
    void shouldExecuteFindAllMethodOfQuestionDaoOnce() {
        //given
        when(questionDao.findAll()).thenReturn(testDataProvider.getListWithOneQuestion());
        TestService sut = new TestServiceImpl(ioService, questionDao);

        //when
        sut.executeTest();

        //then
        verify(questionDao, times(1)).findAll();
    }

}
