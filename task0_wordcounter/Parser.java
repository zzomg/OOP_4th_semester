package oop.java.wordcounter;

import java.io.*;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class Parser {

    private long totalWords = 0;
    private Map<String, Long> words = new HashMap<>();

    private void parseLine(String line) {
        StringBuilder word = new StringBuilder();
        for (Character c : line.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                word.append(c);
            }
            else {
                if (!word.toString().isEmpty()) {
                    ++totalWords;
                    if (words.containsKey(word.toString())) {
                        words.put(word.toString(), words.get(word.toString()) + 1);
                    } else {
                        words.put(word.toString(), (long) 1);
                    }
                    word = new StringBuilder();
                }
            }
        }
    }
    private void parseInputFile(Scanner inputFile) {
        while (inputFile.hasNextLine()) {
            parseLine(inputFile.nextLine());
        }
    }
    private void pushOutputFile(PrintWriter outputFile) {
        Map<String, Long> sortedWords = words
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        for (Map.Entry<String, Long> entry : sortedWords.entrySet()) {
            outputFile.write(entry.getKey() + " " + entry.getValue() + " " +
                    String.format("%.3f", (double)entry.getValue() / (double)totalWords) + "\n");
        }
    }

    public Parser() {}
    public void convert(String inputFilePath, String outputFilePath) {
        try(Scanner inputFile = new Scanner(new File(inputFilePath));
            PrintWriter outputFile = new PrintWriter(new FileOutputStream(outputFilePath))) {
            parseInputFile(inputFile);
            pushOutputFile(outputFile);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
