package ru.vladshi.javalearning.currencyexchange.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapperImpl;

import java.io.IOException;

public class ErrorMessageCreator {

    private static final ResponseJsonMapper jsonMapper = ResponseJsonMapperImpl.INSTANCE;

    public static void writeAsJsonToResponse(HttpServletResponse response, String message) throws IOException {
        ErrorMessage errorMsg = new ErrorMessage(message);
        jsonMapper.writeToResponse(response, errorMsg);
    }
}
