/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.github.tchlyah.search;

import com.github.tchlyah.search.service.Finder;
import com.github.tchlyah.search.service.Indexer;
import com.github.tchlyah.search.util.StringUtils;

import java.util.Map;
import java.util.Scanner;

public class App {

    private static Indexer indexer = new Indexer();

    private static Finder finder = new Finder(indexer);

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No directory given to index.");
        }

        // Index directory
        indexer.index(args[0]);

        var keyboard = new Scanner(System.in);
        while (true) {
            System.out.print("search> ");
            final String line = keyboard.nextLine();
            if (line.equals(":quit")) {
                break;
            }

            Map<String, Double> results = finder.find(line);
            System.out.println(StringUtils.formatResults(results));
        }

    }
}
