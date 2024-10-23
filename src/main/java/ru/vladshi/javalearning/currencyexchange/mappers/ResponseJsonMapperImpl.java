package ru.vladshi.javalearning.currencyexchange.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public enum ResponseJsonMapperImpl implements ResponseJsonMapper {

    INSTANCE;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

//    @Override
//    public String writeValueAsString(Object value) throws JsonProcessingException {
//        return OBJECT_MAPPER.writeValueAsString(value);
//    }

    @Override
    public void writeToResponse(HttpServletResponse response, Object obj) throws IOException {
        String jsonResponse = OBJECT_MAPPER.writeValueAsString(obj);
        response.getWriter().write(jsonResponse);
    }
}
