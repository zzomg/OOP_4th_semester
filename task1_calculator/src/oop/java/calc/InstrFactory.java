package oop.java.calc;

import oop.java.calc.exception.CalcException;
import oop.java.calc.instruction.Instruction;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InstrFactory extends AbstractFactory
{
    public static final Logger LOGGER = Logger.getLogger(InstrFactory.class.getName());

    private Map<String, Class<?>> classMap = new TreeMap<>();

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
    
     private InstrFactory()
     {
         try(InputStream input = InstrFactory.class.getClassLoader().getResourceAsStream("config.properties")) {
             Properties properties = new Properties();
             assert input != null;
             properties.load(input);
             for(Object prop : properties.keySet()) {
                 classMap.put(prop.toString(), Class.forName(properties.getProperty(prop.toString())));
             }
         } catch (IOException e) {
             LOGGER.log(Level.SEVERE, "Unexpected exception: Config file was not found");
             throw new CalcException("Error: Config file was not found");
         } catch (ClassNotFoundException e) {
             LOGGER.log(Level.SEVERE, "Unexpected exception: Instruction was not found");
             throw new CalcException("Error: Instruction was not found");
         }
    }

    @Override
    public Instruction create(String key) {
        Object instr = new Object();
        try {
            if(!classMap.containsKey(key)) {
                LOGGER.log(Level.SEVERE, "Unexpected exception: Invalid instruction name");
                throw new CalcException("Error: Invalid instruction name");
            }
            instr = classMap.get(key).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException |
                IllegalAccessException | InvocationTargetException e) {
            LOGGER.log(Level.SEVERE, "Unexpected exception : Couldn't create instruction");
            throw new CalcException("Error: Couldn't create instruction");
        }
        assert instr instanceof Instruction;
        return (Instruction) instr;
    }

}
