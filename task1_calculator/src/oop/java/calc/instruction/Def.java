package oop.java.calc.instruction;

import oop.java.calc.exception.CalcException;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Def implements Instruction
{
    public static final Logger LOGGER = Logger.getLogger(Def.class.getName());

    @Override
    public void execute(List<Double> stack, List<String> args, Map<String, Double> vars)
    {
        LOGGER.setLevel(Level.FINER);
        if(args.size() != 2) {
            LOGGER.log(Level.SEVERE, "Unexpected exception: DEFINE must have 2 arguments");
            throw new CalcException(String.format("DEFINE must have 2 arguments (name and value), have %d)",
                    args.size()));
        }
        if(!args.get(0).chars().allMatch(Character::isLetter)) {
            LOGGER.log(Level.SEVERE, "Unexpected exception: DEFINE: name must only contain letters");
            throw new CalcException("DEFINE: name must only contain letters");
        }
        if(!args.get(1).chars().allMatch(Character::isDigit)) {
            LOGGER.log(Level.SEVERE, "Unexpected exception: DEFINE: value must be a digit");
            throw new CalcException("DEFINE: value must be a digit");
        }
        //for (Map.Entry<String, Double> entry : vars.entrySet()) {
        if (vars.containsKey(args.get(0))) {
            LOGGER.log(Level.SEVERE, "Unexpected exception: value was already defined");
            throw new CalcException("Error: DEFINE: value was already defined");
        }
        vars.put(args.get(0), Double.parseDouble(args.get(1)));

        LOGGER.log(Level.FINER, "Current stack is {0}:", stack);
    }
}
