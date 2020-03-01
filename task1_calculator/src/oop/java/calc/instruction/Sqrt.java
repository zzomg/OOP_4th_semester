package oop.java.calc.instruction;

import oop.java.calc.exception.CalcException;
import oop.java.calc.Calculator;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.sqrt;

public class Sqrt implements Instruction
{
    public static final Logger LOGGER = Logger.getLogger(Sqrt.class.getName());

    public Sqrt() {}

    @Override
    public void execute(List<Double> stack, List<String> args)
    {
        LOGGER.setLevel(Level.FINER);
        if(args.size() > 0) {
            Calculator.flushDefFile(Calculator.defFilePath);
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            throw new CalcException(String.format("SQRT does not support arguments (must have 0, have %d)",
                    args.size()));
        }
        if(stack.size() < 1) {
            Calculator.flushDefFile(Calculator.defFilePath);
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            throw new CalcException("SQRT: Stack does not contain enough " +
                    "elements to execute instruction (must have at least 1)");
        }
        Double op = stack.remove(stack.size() - 1);
        stack.add(sqrt(op));
        LOGGER.log(Level.FINE, "SQRT: Got square root from {0}", op);
        LOGGER.log(Level.FINER, "Current stack is {0}:", stack);
    }
}
