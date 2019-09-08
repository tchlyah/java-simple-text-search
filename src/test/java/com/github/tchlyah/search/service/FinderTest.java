package com.github.tchlyah.search.service;

import com.github.tchlyah.search.service.Finder;
import com.github.tchlyah.search.service.Indexer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinderTest {

    @Mock
    private Indexer indexer;

    @Spy
    @InjectMocks
    private Finder finder;

    @Captor
    private ArgumentCaptor<Collection<String>> wordsCaptor;

    @ParameterizedTest(name = "{0}")
    @MethodSource
    void findLine(String description, String line, List<String> words) {
        // Given a mock a finder
        doReturn(null).when(finder).find(anyCollection());

        // When we try to find a sentence
        finder.find(line);

        // Then the finder should call find method with normalized list of words
        verify(finder, times(1)).find(wordsCaptor.capture());
        assertThat(wordsCaptor.getValue()).as(description).containsExactlyElementsOf(words);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> findLine() {
        return Stream.of(
                Arguments.of("single word", "test", List.of("test")),
                Arguments.of("Multiple words with Maj", "test Call", List.of("test", "call")),
                Arguments.of("Multiple words with comma", "Test, Call", List.of("test", "call"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource
    void find(String description, List<String> words, Map<String, String> index, Map<String, Double> expected) {
        // Given an indexer mocked with index entry
        doAnswer(answer -> index.get(answer.<String>getArgument(0))).when(indexer).find(anyString());
        doAnswer(answer -> index.containsKey(answer.<String>getArgument(0))).when(indexer).contains(anyString());
        doReturn(index.values()).when(indexer).indexedFiles();

        // When we try to find the list of words
        Map<String, Double> actual = finder.find(words);

        // Then we should get the expected results
        assertThat(actual).as(description).isEqualTo(expected);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> find() {
        return Stream.of(
                Arguments.of("File contains all words",
                        List.of("lorem", "ipsum"),
                        Map.of("lorem", "file_1",
                                "ipsum", "file_1"),
                        Map.of("file_1", 1d)),
                Arguments.of("Multiple files containing some words",
                        List.of("lorem", "ipsum"),
                        Map.of("lorem", "file_1",
                                "ipsum", "file_2"),
                        Map.of("file_1", 0.5,
                                "file_2", 0.5)),
                Arguments.of("Multiple files some with the words and some not",
                        List.of("lorem", "ipsum"),
                        Map.of("lorem", "file_1",
                                "dolor", "file_2"),
                        Map.of("file_1", 0.5,
                                "file_2", 0d))
        );
    }
}
