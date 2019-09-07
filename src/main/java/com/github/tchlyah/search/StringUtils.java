package com.github.tchlyah.search;

import java.util.Arrays;
import java.util.stream.Stream;

public interface StringUtils {

    String WORD_REGEXP = "\\W+";

    static Stream<String> wordStream(String line) {
        return Arrays.stream(line.split(WORD_REGEXP));
    }

    static String normalize(String string) {
        return string.toLowerCase();
    }
}
