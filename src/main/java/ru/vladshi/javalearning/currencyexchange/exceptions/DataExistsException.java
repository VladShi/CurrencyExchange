package ru.vladshi.javalearning.currencyexchange.exceptions;

public class DataExistsException extends RuntimeException {
    public DataExistsException(String messageError) {
        super(messageError);
    }
}