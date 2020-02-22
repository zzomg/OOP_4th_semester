package oop.java.calc;

import java.util.List;

public class Sum implements Instruction{
    private List<String> args;

    public Sum() {}

    @Override
    public void execute(List<String> args) {
        System.out.println(args.toString());
    }
}
