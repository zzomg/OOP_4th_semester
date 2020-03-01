package oop.java.calc.instruction;

import oop.java.calc.InstrFactory;
import oop.java.calc.exception.CalcException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SumTest {

    @Test
    void execute() {
        List<Double> stack = new ArrayList<>();
        List<String> args = new ArrayList<>();

        stack.add((double) 4);
        stack.add((double) 6);

        InstrFactory.getInstance().create("oop.java.calc.instruction.Sum").execute(stack, args);

        Assertions.assertEquals(10, stack.get(0));

        args.add("5");

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("oop.java.calc.instruction.Mul").execute(stack, args));

        stack.clear();
        args.clear();

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("oop.java.calc.instruction.Mul").execute(stack, args));
    }
}