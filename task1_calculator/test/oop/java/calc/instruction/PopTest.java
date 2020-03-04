package oop.java.calc.instruction;

import oop.java.calc.InstrFactory;
import oop.java.calc.exception.CalcException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class PopTest {

    @Test
    void execute() {
        List<Double> stack = new ArrayList<>();
        List<String> args = new ArrayList<>();
        Map<String, Double> vars = new TreeMap<>();

        stack.add((double) 5);

        InstrFactory.getInstance().create("pop").execute(stack, args, vars);

        Assertions.assertEquals(0, stack.size());

        args.add("5");

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("pop").execute(stack, args, vars));

        stack.clear();
        args.clear();

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("pop").execute(stack, args, vars));
    }
}