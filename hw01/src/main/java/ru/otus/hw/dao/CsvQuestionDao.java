package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private static final char SEPARATOR = ';';

    private static final int SKIP_LINES = 1;

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        ClassLoader classLoader = getClass().getClassLoader();
        String fileName = fileNameProvider.getTestFileName();

        try (InputStream inputStream = requireNonNull(classLoader.getResourceAsStream(fileName));
             InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            return buildCsvToBeans(reader).stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();

        } catch (IOException | NullPointerException e) {
            throw new QuestionReadException("The file %s could not be read"
                    .formatted(fileNameProvider.getTestFileName()), e);
        }
    }

    private static CsvToBean<QuestionDto> buildCsvToBeans(Reader reader) {
        return new CsvToBeanBuilder<QuestionDto>(reader)
                .withSkipLines(SKIP_LINES)
                .withIgnoreLeadingWhiteSpace(true)
                .withSeparator(SEPARATOR)
                .withType(QuestionDto.class)
                .build();
    }
}
