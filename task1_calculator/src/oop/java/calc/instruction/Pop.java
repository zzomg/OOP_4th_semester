package oop.java.calc.instruction;

import oop.java.calc.exception.CalcException;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pop implements Instruction
{
    public static final Logger LOGGER = Logger.getLogger(Pop.class.getName());

    @Override
    public void execute(List<Double> stack, List<String> args, Map<String, Double> vars)
    {
        LOGGER.setLevel(Level.FINER);
        if(args.size() > 0) {
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            throw new CalcException(String.format("SUM does not support arguments (must have 0, have %d)",
                    args.size()));
        }
        if(stack.size() < 1) {
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            throw new CalcException("POP: Stack does not contain enough " +
                    "elements to execute instruction (must have at least 1)");
        }
        Double op = stack.remove(stack.size() - 1);
        LOGGER.log(Level.FINE, "POP: Last element {0} was removed from stack", op);
        LOGGER.log(Level.FINER, "Current stack is {0}:", stack);
    }
}