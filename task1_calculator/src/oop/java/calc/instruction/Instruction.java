package oop.java.calc.instruction;

import java.util.List;

public interface Instruction {
    void execute(List<Double> stack, List<String> args);
    //TODO: void execute(List<Double> stack, List<String> args, Map<String, Double> vars);
}
