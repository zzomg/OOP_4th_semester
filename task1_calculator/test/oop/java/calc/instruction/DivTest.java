package oop.java.calc.instruction;

import oop.java.calc.Calculator;
import oop.java.calc.InstrFactory;
import oop.java.calc.exception.CalcException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DivTest
{
    @org.junit.jupiter.api.Test
    void execute()
    {
        List<Double> stack = new ArrayList<>();
        List<String> args = new ArrayList<>();
        Map<String, Double> vars = new TreeMap<>();

        stack.add((double) 5);
        stack.add((double) 10);

        InstrFactory.getInstance().create("/").execute(stack, args, vars);

        Assertions.assertEquals(2, stack.get(0));

        args.add("5");

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("/").execute(stack, args, vars));

        stack.clear();
        args.clear();

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("/").execute(stack, args, vars));
    }
}