package ru.vladshi.javalearning.currencyexchange.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ErrorMessageCreator {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static String getAsJson(String message) throws JsonProcessingException {
        ErrorMessage errorMsg = new ErrorMessage(message);
        return jsonMapper.writeValueAsString(errorMsg);
    }
}
