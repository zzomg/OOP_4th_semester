package oop.java.calc;

import java.util.ArrayList;
import java.util.List;

public class Calculator {
//    public static void main(String[] args)  {
//        Calculator calc = new Calculator();
//        String packName = calc.getClass().getPackage().getName();
//        List<String> line = new ArrayList<>();
//        line.add("Hello");
//        line.add("World");
//
//        InstrFactory.getInstance().create(packName + "." + "Sum", line).execute(line);
//    }

    public double calculate(String inputFileName) {
        Parser parser = new Parser();
        parser.parseInputFile(inputFileName);
    }
}
