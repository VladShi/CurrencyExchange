package ru.vladshi.javalearning.currencyexchange.mappers;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ResponseJsonMapper {

//    String writeValueAsString(Object value) throws JsonProcessingException;

    void writeToResponse(HttpServletResponse response, Object obj) throws IOException;
}
