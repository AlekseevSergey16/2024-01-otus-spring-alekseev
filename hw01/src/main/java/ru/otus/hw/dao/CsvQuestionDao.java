package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.Objects.*;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private static final char SEPARATOR = ';';
    private static final int SKIP_LINES = 1;

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/


        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = requireNonNull(classLoader.getResourceAsStream(fileNameProvider.getTestFileName()));
             InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            return buildCsvToBeans(reader).stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();

        } catch (IOException e) {
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
