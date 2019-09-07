package com.github.tchlyah.search;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Finder {

    private final Indexer indexer;

    public Map<String, Double> find(String line) {
        return find(StringUtils.wordStream(line)
                .map(StringUtils::normalize)
                .collect(Collectors.toList()));
    }

    public Map<String, Double> find(Collection<String> words) {
        Map<String, Double> results = words.stream()
                .filter(indexer::contains)
                .collect(Collectors.groupingBy(indexer::find, Collectors.collectingAndThen(Collectors.counting(), c -> c / (double) words.size())));
        indexer.indexedFiles().forEach(file -> results.putIfAbsent(file, 0d));
        return results;
    }
}
