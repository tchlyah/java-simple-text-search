/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.github.tchlyah.search;

import java.util.Scanner;

import static java.lang.String.format;

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
            finder.find(line).forEach((file, rate) -> System.out.println(format("%s : %.2f%%", file, rate * 100)));
        }

    }
}
