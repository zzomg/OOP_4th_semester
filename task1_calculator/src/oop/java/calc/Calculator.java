package oop.java.calc;

import oop.java.calc.exception.CalcException;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Calculator
{
    public static final Logger LOGGER = Logger.getLogger(Calculator.class.getName());

    public List<Double> stack = new ArrayList<>();
    public Map<String, Double> vars = new TreeMap<>();
    
    // TODO: string -> inputStream
    public double calculate(String inputSourceName) throws FileNotFoundException
    {
        LOGGER.setLevel(Level.FINER);
        double result = 0;
        InputStream inputStream;
        if (inputSourceName.isEmpty()) {
            inputStream = System.in;
            LOGGER.log(Level.INFO, "START: input stream is stdin");
        } else {
            inputStream = new FileInputStream(inputSourceName);
            LOGGER.log(Level.INFO, "START: input stream is {0}", inputSourceName);
        }
        String instrName = "";
        List<String> args = new ArrayList<>();
        try (Scanner inputFile = new Scanner(inputStream)) {
            while(inputFile.hasNextLine()) {
                String line = inputFile.nextLine();
                StringTokenizer tokenizer = new StringTokenizer(line);
                int countTokens = tokenizer.countTokens();
                while (tokenizer.hasMoreTokens()) {
                    if(tokenizer.countTokens() == countTokens) {
                        instrName = tokenizer.nextToken().toLowerCase();
                    }
                    if(tokenizer.countTokens() > 0) {
                        args.add(tokenizer.nextToken());
                    }
                }
                if (!instrName.startsWith("#") && countTokens > 0) {
                    InstrFactory.getInstance().create(instrName).execute(stack, args, vars);
                }
                args.clear();
            }
        }
        if (stack.size() > 1) {
            LOGGER.log(Level.SEVERE, "Unexpected exception: Result can not be obtained");
            throw new CalcException("Result can not be obtained: Stack contains more than 1 element.");
        } else if (stack.size() == 0) {
            LOGGER.log(Level.WARNING, "FINISH: Result can not be obtained: Stack is empty.");
        } else {
            result = stack.get(0);
        }
        LOGGER.log(Level.INFO, "FINISH: Result is {0}", result);
        return result;
    }
}
