package ru.vladshi.javalearning.currencyexchange.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vladshi.javalearning.currencyexchange.mappers.DtoMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapperImpl;
import ru.vladshi.javalearning.currencyexchange.models.Currency;
import ru.vladshi.javalearning.currencyexchange.services.CurrencyService;
import ru.vladshi.javalearning.currencyexchange.services.CurrencyServiceImpl;
import ru.vladshi.javalearning.currencyexchange.util.InputValidator;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyServiceImpl.INSTANCE;
    private final ResponseJsonMapper jsonMapper = ResponseJsonMapperImpl.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String currencyCode = InputValidator.getValidatedCurrencyCode(request);
        Currency currency = currencyService.getCurrencyByCode(currencyCode);
        jsonMapper.write(response.getWriter(), DtoMapper.toResponseDTO(currency));
    }
}
