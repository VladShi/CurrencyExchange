package ru.vladshi.javalearning.currencyexchange.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseMapper;

public class ErrorMessageCreator {

    private static final ResponseMapper jsonMapper = ResponseMapper.INSTANCE;

    public static String getAsJson(String message) throws JsonProcessingException {
        ErrorMessage errorMsg = new ErrorMessage(message);
        return jsonMapper.writeValueAsString(errorMsg);
    }
}
