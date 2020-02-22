package oop.java.calc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {
    public Parser() {}

    public void parseInputFile(String inputFileName) {
        try (Scanner inputFile = new Scanner(new File(inputFileName))) {

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
