package oop.java.calc.instruction;

import oop.java.calc.InstrFactory;
import oop.java.calc.exception.CalcException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class PushTest {

    @Test
    void execute() {
        List<Double> stack = new ArrayList<>();
        List<String> args = new ArrayList<>();
        Map<String, Double> vars = new TreeMap<>();

        stack.add((double) 5);
        args.add("10");

        int stackPrevSize = stack.size();

        InstrFactory.getInstance().create("push").execute(stack, args, vars);

        Assertions.assertEquals(stackPrevSize + 1, stack.size());

        args.clear();
        args.add("A");
        vars.put("A", (double) 5);

        stackPrevSize = stack.size();

        InstrFactory.getInstance().create("push").execute(stack, args, vars);
        Assertions.assertEquals(stackPrevSize + 1, stack.size());

        args.clear();
        args.add("B");

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("push").execute(stack, args, vars));

        args.clear();
        args.add("5utngg8rgn3%$%@");

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("push").execute(stack, args, vars));
    }
}