package oop.java.calc.instruction;

import oop.java.calc.exception.CalcException;
import oop.java.calc.Calculator;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sub implements Instruction
{
    public static final Logger LOGGER = Logger.getLogger(Sub.class.getName());

    @Override
    public void execute(List<Double> stack, List<String> args, Map<String, Double> vars)
    {
        LOGGER.setLevel(Level.FINER);
        if (args.size() > 0) {
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            throw new CalcException(String.format("SUB does not support arguments (must have 0, have %d)",
                    args.size()));
        }
        if (stack.size() < 2) {
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            throw new CalcException("SUB: Stack does not contain enough " +
                    "elements to execute instruction (must have at least 2)");
        }
        Double op1, op2;
        op1 = stack.remove(stack.size() - 1);
        op2 = stack.remove(stack.size() - 1);
        stack.add(op1 - op2);
        LOGGER.log(Level.FINE, "SUB: Subtracted {0} - {1}", new Object[] {op1, op2});
        LOGGER.log(Level.FINER, "Current stack is {0}:", stack);
    }
}
