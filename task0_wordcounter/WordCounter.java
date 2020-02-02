package oop.java.wordcounter;

public class WordCounter {
    public static void main(String[] args) {
        Parser parser = new Parser();
        parser.convert("input.txt", "output.txt");
    }
}
