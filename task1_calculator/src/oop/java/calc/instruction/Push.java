package oop.java.calc.instruction;

import oop.java.calc.exception.CalcException;
import oop.java.calc.Calculator;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Push implements Instruction
{
    public static final Logger LOGGER = Logger.getLogger(Push.class.getName());

    public Push() {}

    @Override
    public void execute(List<Double> stack, List<String> args)
    {
        LOGGER.setLevel(Level.FINER);
        if(args.size() != 1) {
            Calculator.flushDefFile(Calculator.defFilePath);
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            throw new CalcException(String.format("PUSH must have 1 argument, have %d",
                    args.size()));
        }
        stack.add(Double.parseDouble(args.get(0)));
        LOGGER.log(Level.FINE, "PUSH: Added last element {0} to stack", args.get(0));
        LOGGER.log(Level.FINER, "Current stack is {0}:", stack);
    }
}
