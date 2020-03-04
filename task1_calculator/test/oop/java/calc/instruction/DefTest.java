package oop.java.calc.instruction;

import oop.java.calc.InstrFactory;
import oop.java.calc.exception.CalcException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class DefTest {

    @Test
    void execute()
    {
        List<Double> stack = new ArrayList<>();
        List<String> args = new ArrayList<>();
        Map<String, Double> vars = new TreeMap<>();

        args.add("A");
        args.add("5");

        InstrFactory.getInstance().create("define").execute(stack, args, vars);

        Double actualDefValue = vars.get("A");

        Assertions.assertEquals(5, actualDefValue);

        args.clear();
        vars.clear();

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("define").execute(stack, args, vars));

        vars.clear();
        args.add("1");
        args.add("2");

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("define").execute(stack, args, vars));

        vars.clear();
        args.clear();

        args.add("A");
        args.add("B");

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("define").execute(stack, args, vars));

        vars.clear();
        args.clear();

        args.add("A");
        args.add("5");
        args.add("A");
        args.add("5");

        Assertions.assertThrows(CalcException.class,
                () -> InstrFactory.getInstance().create("define").execute(stack, args, vars));
    }
}