package oop.java.calc.instruction;

import oop.java.calc.exception.CalcException;
import oop.java.calc.Calculator;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Print implements Instruction
{
    public static final Logger LOGGER = Logger.getLogger(Print.class.getName());

    @Override
    public void execute(List<Double> stack, List<String> args, Map<String, Double> vars)
    {
        LOGGER.setLevel(Level.FINER);
        if(args.size() > 0) {
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            throw new CalcException(String.format("PRINT does not support arguments (must have 0, have %d)",
                    args.size()));
        }
        if(stack.size() < 1) {
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            throw new CalcException("PRINT: Stack does not contain enough " +
                    "elements to execute instruction (must have at least 1)");
        }
        System.out.println(stack.get(stack.size() - 1));
        LOGGER.log(Level.FINER, "Current stack is {0}:", stack);
    }
}
