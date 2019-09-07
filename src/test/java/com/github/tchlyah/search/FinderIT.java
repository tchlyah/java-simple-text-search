package com.github.tchlyah.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static com.github.tchlyah.search.IndexerTest.SEARCH_DIR;
import static java.util.Map.of;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test class to test the complete flow without any mock
 */
class FinderIT {

    private Indexer indexer;

    private Finder finder;

    @BeforeEach
    void initTest() {
        indexer = new Indexer();
        finder = new Finder(indexer);
    }

    @ParameterizedTest
    @MethodSource
    void find(String directory, String line, Map<String, Double> expected) {
        // Given we index a directory
        indexer.index(directory);
        // When we try to find a sentence
        Map<String, Double> results = finder.find(line);

        // Then the result should be correct
        assertThat(results).isEqualTo(expected);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> find() {
        return Stream.of(
                Arguments.of(SEARCH_DIR, "lorem Ipsum", of(
                        "lorem ipsum.txt", 1d,
                        "finibus_bonorum_1.txt", 0d,
                        "finibus-bonorum-2.txt", 0d)),
                Arguments.of(SEARCH_DIR, "quo voluptas nulla pariatur", of(
                        "lorem ipsum.txt", 0.5d,
                        "finibus_bonorum_1.txt", 0.5d,
                        "finibus-bonorum-2.txt", 0d))
        );
    }
}
