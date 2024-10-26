package ru.vladshi.javalearning.currencyexchange.mappers;

import java.io.IOException;
import java.io.PrintWriter;

public interface ResponseJsonMapper {

//    String writeValueAsString(Object value) throws JsonProcessingException;

    void write(PrintWriter writer, Object obj) throws IOException;
}
