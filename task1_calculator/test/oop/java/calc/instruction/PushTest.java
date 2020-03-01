package oop.java.calc.instruction;

import oop.java.calc.InstrFactory;
import oop.java.calc.exception.CalcException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PushTest {

    @Test
    void execute() {
        List<Double> stack = new ArrayList<>();
        List<String> args = new ArrayList<>();

        stack.add((double) 5);
        args.add("10");

        int stackPrevSize = stack.size();

        InstrFactory.getInstance().create("oop.java.calc.instruction.Push").execute(stack, args);

        Assertions.assertEquals(stackPrevSize + 1, stack.size());

        args.add("5");

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("oop.java.calc.instruction.Mul").execute(stack, args));
    }
}