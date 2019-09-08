# Java Simple Text Search

This is sample command line text search java application implementing a simple in-memory indexer and search engine.

## Requirements

- Java 11

## Build and test App

```bash
$ ./gradlew clean build --info
```

## Run App

```bash
$ ./gradlew run -q --console=plain --args="src/test/resources/search"
```

## description

The console application takes a single argument: directory containing text files to be indexed.

The application build an in memory representation of the files and their content in the given directory, and then give a command prompt at which interactive searches can be performed.

Example: 

```bash
$ ./gradlew run -q --console=plain --args="/foo/bar"

Indexing directory '/foo/bar'
3 files indexed in directory '/foo/bar' in 21 ms
search> to be or not to be
file1.txt : 100,00%
file2.txt : 90%
search> cats
no matches found
search> :quit
$ 
```

## Ranking algorithm

For now, the ranking algorithm is very simple:
* It is the percentage of the found words of total words searched: ![\Large rank = \frac{n_{found}}{count} \times 100](https://latex.codecogs.com/svg.latex?rank =\frac{n_{found}}{count}\times100)
    * If it contains none of the words: 0%
    * If it contains all the words in any order: 100%
