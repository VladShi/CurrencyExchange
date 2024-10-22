package ru.vladshi.javalearning.currencyexchange.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public enum ResponseMapper {

    INSTANCE;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public String writeValueAsString(Object value) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(value);
    }

    public void writeToResponse(HttpServletResponse response, Object obj) throws IOException {
        String jsonResponse = OBJECT_MAPPER.writeValueAsString(obj);
        response.getWriter().write(jsonResponse);
    }
}
