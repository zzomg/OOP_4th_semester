package oop.java.calc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class InstrFactory extends AbstractFactory {
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
    public Instruction create(String key, List<String> args) {
        Class<?> instrCls;
        Object instr = new Object();
        try {
            instrCls = Class.forName(key);
            instr = instrCls.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace(System.err);
        }
        assert instr instanceof Instruction;
        return (Instruction) instr;
    }

}
