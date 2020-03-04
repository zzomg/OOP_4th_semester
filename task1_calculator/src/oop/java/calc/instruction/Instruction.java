package oop.java.calc.instruction;

import java.util.List;
import java.util.Map;

public interface Instruction {
    void execute(List<Double> stack, List<String> args, Map<String, Double> vars);
    //TODO: void execute(List<Double> stack, List<String> args, Map<String, Double> vars);
}
