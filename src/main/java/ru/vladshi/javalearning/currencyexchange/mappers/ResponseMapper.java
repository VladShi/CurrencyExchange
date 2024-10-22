package ru.vladshi.javalearning.currencyexchange.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseMapper {

    private static final ResponseMapper INSTANCE = new ResponseMapper();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ResponseMapper() {
    }

    public static ResponseMapper getInstance() {
        return INSTANCE;
    }

    public String writeValueAsString(Object value) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(value);
    }

    public void writeToResponse(HttpServletResponse response, Object obj) throws IOException {
        String jsonResponse = OBJECT_MAPPER.writeValueAsString(obj);
        response.getWriter().write(jsonResponse);
    }
}
