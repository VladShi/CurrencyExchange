package ru.vladshi.javalearning.currencyexchange.exceptions;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String messageError) {
        super(messageError);
    }
}