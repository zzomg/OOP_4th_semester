package oop.java.calc;

import java.util.List;

public abstract class AbstractFactory {
    abstract Instruction create(String key, List<String> args);
}
