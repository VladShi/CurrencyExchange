package ru.vladshi.javalearning.currencyexchange.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;

public enum ResponseJsonMapperImpl implements ResponseJsonMapper {

    INSTANCE;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void write(PrintWriter writer, Object obj) throws IOException {
        OBJECT_MAPPER.writeValue(writer, obj);
    }
}
