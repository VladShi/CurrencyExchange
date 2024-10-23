package ru.vladshi.javalearning.currencyexchange.exceptions;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class ExceptionHandler {

    private ExceptionHandler() {}

    public static void handleException(HttpServletResponse servletResponse, Throwable throwable) throws IOException {
        servletResponse.setStatus(getStatusCode(throwable));
        ErrorMessageCreator.writeAsJsonToResponse(servletResponse, throwable.getMessage());
    }

    private static int getStatusCode(Throwable throwable) {
        return switch (throwable) {
            case DatabaseException e -> SC_INTERNAL_SERVER_ERROR;
            default -> throw new RuntimeException("There is no status code for this exception: "
                                                        + throwable.getClass().getSimpleName());
        };
    }
}
