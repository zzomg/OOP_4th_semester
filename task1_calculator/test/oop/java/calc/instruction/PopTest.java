package oop.java.calc.instruction;

import oop.java.calc.InstrFactory;
import oop.java.calc.exception.CalcException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PopTest {

    @Test
    void execute() {
        List<Double> stack = new ArrayList<>();
        List<String> args = new ArrayList<>();

        stack.add((double) 5);

        InstrFactory.getInstance().create("oop.java.calc.instruction.Pop").execute(stack, args);

        Assertions.assertEquals(0, stack.size());

        args.add("5");

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("oop.java.calc.instruction.Mul").execute(stack, args));

        stack.clear();
        args.clear();

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("oop.java.calc.instruction.Mul").execute(stack, args));
    }
}