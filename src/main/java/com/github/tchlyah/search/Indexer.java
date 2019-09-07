package com.github.tchlyah.search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.StreamSupport;

import static java.lang.String.format;

public class Indexer {

    private final Map<String, String> index = new HashMap<>();

    private final Set<String> indexedFiles = new HashSet<>();

    public void index(String directory) {
        try (var dirStream = Files.newDirectoryStream(Paths.get(directory))) {
            System.out.println(format("Indexing directory '%s'", directory));
            var start = Instant.now();
            StreamSupport.stream(dirStream.spliterator(), false)
                    .forEach(this::index);
            var finish = Instant.now();
            System.out.println(format("%d files indexed in directory '%s' in %d ms", indexedFiles.size(), directory, Duration.between(start, finish).toMillis()));
        } catch (IOException e) {
            System.err.println(format("Unable to read directory '%s'", directory));
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void index(Path file) {
        var fileName = file.getFileName().toString();
        try {
            Files.lines(file)
                    .flatMap(StringUtils::wordStream)
                    .map(StringUtils::normalize)
                    .forEach(word -> index.put(word, fileName));
            indexedFiles.add(fileName);
        } catch (IOException e) {
            System.err.println(format("Unable to read file '%s'", file));
            e.printStackTrace();
        }
    }

    public String find(String word) {
        return index.get(word);
    }

    public boolean contains(String word) {
        return index.containsKey(word);
    }

    public Collection<String> indexedFiles() {
        return Set.copyOf(indexedFiles);
    }
}
