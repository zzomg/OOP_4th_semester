package oop.java.calc.exception;

public class CalcException extends RuntimeException
{
    public CalcException(String message) {
        super(message);
    }

    public CalcException(String message, Throwable cause) {
        super(message, cause);
    }
}
