package com.github.tchlyah.search;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource
    void wordStream(String description, String line, List<String> expected) {
        List<String> actual = StringUtils.wordStream(line).collect(Collectors.toList());
        assertThat(actual).as(description).isEqualTo(expected);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> wordStream() {
        return Stream.of(
                Arguments.of("Single word", "word", of("word")),
                Arguments.of("Multiple words", "hello world", of("hello", "world")),
                Arguments.of("Multiple words with comma and period", "hello, world.", of("hello", "world")),
                Arguments.of("Multiple lines", "hello\nworld", of("hello", "world"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource
    void normalize(String description, String word, String expected) {
        assertThat(StringUtils.normalize(word)).as(description).isEqualTo(expected);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> normalize() {
        return Stream.of(
                Arguments.of("simple word", "word", "word"),
                Arguments.of("Capitalized words", "wOrd", "word")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource
    void formatResults(String description, Map<String, Double> results, String expected) {
        assertThat(StringUtils.formatResults(results)).as(description).isEqualTo(expected);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> formatResults() {
        return Stream.of(
                Arguments.of("single result", Map.of("file_1", 1d), "file_1 : 100,00%"),
                Arguments.of("single non null result", Map.of(
                        "file_1", 0.5d,
                        "file_2", 0d),
                        "file_1 : 50,00%"),
                Arguments.of("multiple non ordered results", Map.of(
                        "file_1", 0.5d,
                        "file_2", 0.75d,
                        "file_3", 1d,
                        "file_4", 0d),
                        "file_3 : 100,00%\n" +
                        "file_2 : 75,00%\n" +
                        "file_1 : 50,00%")
        );
    }
}
