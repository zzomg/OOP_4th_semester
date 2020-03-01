package oop.java.calc.instruction;

import oop.java.calc.Calculator;
import oop.java.calc.InstrFactory;
import oop.java.calc.exception.CalcException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class DivTest
{
    @org.junit.jupiter.api.Test
    void execute()
    {
        List<Double> stack = new ArrayList<>();
        List<String> args = new ArrayList<>();

        stack.add((double) 5);
        stack.add((double) 10);

        InstrFactory.getInstance().create("oop.java.calc.instruction.Div").execute(stack, args);

        Assertions.assertEquals(2, stack.get(0));

        args.add("5");

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("oop.java.calc.instruction.Div").execute(stack, args));

        stack.clear();
        args.clear();

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("oop.java.calc.instruction.Div").execute(stack, args));
    }
}