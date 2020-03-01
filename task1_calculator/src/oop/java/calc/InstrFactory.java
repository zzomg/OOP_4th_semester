package oop.java.calc;

import oop.java.calc.instruction.Instruction;
import oop.java.calc.instruction.Sum;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InstrFactory extends AbstractFactory
{
    public static final Logger LOGGER = Logger.getLogger(InstrFactory.class.getName());

    private static volatile InstrFactory instance;

    public static InstrFactory getInstance() {
        InstrFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (InstrFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new InstrFactory();
                }
            }
        }
        return localInstance;
    }

    @Override
    public Instruction create(String key) {
        Class<?> instrCls;
        Object instr = new Object();
        try {
            instrCls = Class.forName(key);
            instr = instrCls.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            LOGGER.log(Level.SEVERE, "Unexpected exception");
            e.printStackTrace();
        }
        assert instr instanceof Instruction;
        return (Instruction) instr;
    }

}
