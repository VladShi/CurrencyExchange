package ru.vladshi.javalearning.currencyexchange.exceptions;

import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapperImpl;

import java.io.IOException;
import java.io.PrintWriter;

public class ErrorMessageCreator {

    private static final ResponseJsonMapper jsonMapper = ResponseJsonMapperImpl.INSTANCE;

    public static void writeAsJsonTo(PrintWriter writer, String message) throws IOException {
        ErrorMessage errorMsg = new ErrorMessage(message);
        jsonMapper.write(writer, errorMsg);
    }
}
