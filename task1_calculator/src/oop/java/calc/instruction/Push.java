package oop.java.calc.instruction;

import oop.java.calc.exception.CalcException;
import oop.java.calc.Calculator;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Push implements Instruction
{
    public static final Logger LOGGER = Logger.getLogger(Push.class.getName());

    @Override
    public void execute(List<Double> stack, List<String> args, Map<String, Double> vars)
    {
        LOGGER.setLevel(Level.FINER);
        if(args.size() != 1) {
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            throw new CalcException(String.format("PUSH must have 1 argument, have %d",
                    args.size()));
        }

        if(args.get(0).chars().allMatch(Character::isLetter)) {
            if(!vars.containsKey(args.get(0))) {
                throw new CalcException("PUSH: Error: value was not defined previously");
            }
            stack.add(vars.get(args.get(0)));
        } else if (args.get(0).chars().allMatch(Character::isDigit)) {
            stack.add(Double.parseDouble(args.get(0)));
        } else {
            throw new CalcException("PUSH: Error: incorrect value");
        }

        LOGGER.log(Level.FINE, "PUSH: Added last element {0} to stack", args.get(0));
        LOGGER.log(Level.FINER, "Current stack is {0}:", stack);
    }
}
