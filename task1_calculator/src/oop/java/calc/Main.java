package oop.java.calc;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException
    {
        Calculator calc = new Calculator();
        System.out.println(calc.calculate("input.txt"));
    }
}
