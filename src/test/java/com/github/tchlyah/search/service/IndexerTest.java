package com.github.tchlyah.search.service;

import com.github.tchlyah.search.service.Indexer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexerTest {

    static final String SEARCH_DIR = "src/test/resources/search/";

    @Spy
    private Indexer indexer;

    @Captor
    private ArgumentCaptor<Path> pathArgumentCaptor;

    @Test
    void indexDirectory() {
        // Given a mocked indexer
        doNothing().when(indexer).index(any(Path.class));

        // When we index a directory
        indexer.index(SEARCH_DIR);

        // Then the indexer should index all the files in this directory
        verify(indexer, times(3)).index(pathArgumentCaptor.capture());
        assertThat(pathArgumentCaptor.getAllValues()).containsExactlyInAnyOrder(
                Path.of(SEARCH_DIR + "lorem ipsum.txt"),
                Path.of(SEARCH_DIR + "finibus_bonorum_1.txt"),
                Path.of(SEARCH_DIR + "finibus-bonorum-2.txt"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource
    void indexFile(String file, List<String> indexedWords, List<String> nonIndexedWords) {
        // When we index a file
        indexer.index(Path.of(SEARCH_DIR + file));

        // Then the file should be indexed
        assertThat(indexer.indexedFiles()).containsExactly(file);

        // And the index should contains all the words in the file
        indexedWords.forEach(word -> assertThat(indexer.find(word)).isEqualTo(file));
        indexedWords.forEach(word -> assertThat(indexer.contains(word)).isTrue());

        // and shouldn't contain the words not present in the file
        nonIndexedWords.forEach(word -> assertThat(indexer.find(word)).isNull());
        nonIndexedWords.forEach(word -> assertThat(indexer.contains(word)).isFalse());
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> indexFile() {
        return Stream.of(
                Arguments.of("lorem ipsum.txt", of("lorem", "ipsum"), of("ullam", "null")),
                Arguments.of("finibus_bonorum_1.txt", of("dolor", "nisi", "consequatur", "ut"), of("itaque", "lorem")),
                Arguments.of("finibus-bonorum-2.txt", of("impedit", "sapiente", "at", "itaque"), of("ani", "repel"))
        );
    }
}
