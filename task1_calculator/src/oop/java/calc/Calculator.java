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

    public static final String defFilePath = "src/defined.properties";
    
    //TODO: убрать обращение к файлам
    public static void flushDefFile(String defFileName)
    {
        try {
            RandomAccessFile defFile = new RandomAccessFile(defFileName, "rw");
            defFile.setLength(0L);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                        args.add(checkValueDefined(tokenizer.nextToken(), instrName));
                    }
                }
                if (!instrName.startsWith("#") && countTokens > 0) {
                    InstrFactory.getInstance().create(getInstrClass(instrName)).execute(stack, args);
                }
                args.clear();
            }
        }
        if (stack.size() > 1) {
            flushDefFile(defFilePath);
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            throw new CalcException("Result can not be obtained: Stack contains more than 1 element.");
        } else if (stack.size() == 0) {
            LOGGER.log(Level.WARNING, "FINISH: Result can not be obtained: Stack is empty.");
        } else {
            result = stack.get(0);
        }
        flushDefFile(defFilePath);
        LOGGER.log(Level.INFO, "FINISH: Result is {0}", result);
        return result;
    }

    private String getInstrClass(String instrName)
    {
        String instrClass = "";
        try(InputStream input = Calculator.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            assert input != null;
            prop.load(input);
            instrClass = prop.getProperty(instrName, instrName);
        } catch (IOException e) {
            flushDefFile(defFilePath);
            e.printStackTrace();
        }
        return instrClass;
    }

    private String checkValueDefined(String token, String instrName)
    {
        String def = "";
        try(InputStream input = new FileInputStream("src/defined.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            if (instrName.equals("define")) {
                for(Object key : prop.keySet()) {
                    if(token.equals(key.toString())) {
                        flushDefFile(defFilePath);
                        throw new CalcException("Error: Value was already defined");
                    }
                }
            }
            def = prop.getProperty(token, token);
        } catch (IOException e) {
            flushDefFile(defFilePath);
            e.printStackTrace();
        }
        return def;
    }
}
