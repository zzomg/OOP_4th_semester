package oop.java.calc;

import oop.java.calc.instruction.Instruction;

public abstract class AbstractFactory {
    abstract Instruction create(String key);
}
