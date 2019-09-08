package com.github.tchlyah.search;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.format;

public interface StringUtils {

    String WORD_REGEXP = "\\W+";
    int MAX_SIZE = 10;

    static Stream<String> wordStream(String line) {
        return Arrays.stream(line.split(WORD_REGEXP));
    }

    static String normalize(String string) {
        return string.toLowerCase();
    }

    static String formatResults(Map<String, Double> results) {
        return results.entrySet().stream()
                .filter(e -> e.getValue() != 0d)
                .limit(MAX_SIZE)
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                .map(e -> format("%s : %.2f%%", e.getKey(), e.getValue() * 100))
                .reduce((s1, s2) -> String.join("\n", s1, s2))
                .orElse("no matches found");
    }
}
