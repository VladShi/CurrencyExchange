package ru.vladshi.javalearning.currencyexchange.mappers;

import java.io.IOException;
import java.io.PrintWriter;

public interface ResponseJsonMapper {

    void write(PrintWriter writer, Object obj) throws IOException;
}
