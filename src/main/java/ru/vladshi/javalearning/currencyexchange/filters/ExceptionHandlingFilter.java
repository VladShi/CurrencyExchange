package ru.vladshi.javalearning.currencyexchange.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vladshi.javalearning.currencyexchange.exceptions.ExceptionHandler;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlingFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException {
        try {
            super.doFilter(request, response, chain);
        } catch (Throwable throwable) {
            ExceptionHandler.handleException(response, throwable);
        }
    }
}
