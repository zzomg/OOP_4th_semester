package oop.java.calc.instruction;

import oop.java.calc.InstrFactory;
import oop.java.calc.exception.CalcException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class DefTest {

    public static final String defFilePath = "src/defined.properties";

    public static void flushDefFile(String defFileName)
    {
        try {
            RandomAccessFile defFile = new RandomAccessFile(defFileName, "rw");
            defFile.setLength(0L);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String checkValueDefined(String token)
    {
        String def = "";
        try(InputStream input = new FileInputStream("src/defined.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            def = prop.getProperty(token, token);
        } catch (IOException e) {
            flushDefFile(defFilePath);
            e.printStackTrace();
        }
        return def;
    }

    @Test
    void execute() {
        List<Double> stack = new ArrayList<>();
        List<String> args = new ArrayList<>();

        args.add("A");
        args.add("5");

        InstrFactory.getInstance().create("oop.java.calc.instruction.Def").execute(stack, args);

        String actualDefValue = checkValueDefined("A");

        Assertions.assertEquals("5", actualDefValue);

        args.clear();

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("oop.java.calc.instruction.Def").execute(stack, args));

        args.add("1");

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("oop.java.calc.instruction.Def").execute(stack, args));

        args.clear();
        args.add("A");
        args.add("B");

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("oop.java.calc.instruction.Def").execute(stack, args));
    }
}