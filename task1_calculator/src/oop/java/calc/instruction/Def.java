package oop.java.calc.instruction;

import oop.java.calc.exception.CalcException;
import oop.java.calc.Calculator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Def implements Instruction
{
    public static final Logger LOGGER = Logger.getLogger(Def.class.getName());

    public Def() {}

    @Override
    public void execute(List<Double> stack, List<String> args)
    {
        LOGGER.setLevel(Level.FINER);
        if(args.size() != 2) {
            Calculator.flushDefFile(Calculator.defFilePath);
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            throw new CalcException(String.format("DEFINE must have 2 arguments (name and value), have %d)",
                    args.size()));
        }
        if(!args.get(0).chars().allMatch(Character::isLetter)) {
            Calculator.flushDefFile(Calculator.defFilePath);
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            throw new CalcException("DEFINE: name must only contain letters");
        }
        if(!args.get(1).chars().allMatch(Character::isDigit)) {
            Calculator.flushDefFile(Calculator.defFilePath);
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            throw new CalcException("DEFINE: value must be a digit");
        }
        try(OutputStream output = new FileOutputStream(Calculator.defFilePath, true)) {
            Properties prop = new Properties();
            prop.setProperty(args.get(0), args.get(1));
            prop.store(output, null);
            LOGGER.log(Level.FINE, "DEFINE: Defined {0} -> {1}", new Object[] {args.get(0), args.get(1)});
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.log(Level.FINER, "Current stack is {0}:", stack);
    }
}
