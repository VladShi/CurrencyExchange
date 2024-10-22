package ru.vladshi.javalearning.currencyexchange.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String messageError) {
        super(messageError);
    }
}
