package ru.vladshi.javalearning.currencyexchange.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vladshi.javalearning.currencyexchange.mappers.DtoMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapperImpl;
import ru.vladshi.javalearning.currencyexchange.models.ExchangeRate;
import ru.vladshi.javalearning.currencyexchange.services.ExchangeRateService;
import ru.vladshi.javalearning.currencyexchange.services.ExchangeRateServiceImpl;
import ru.vladshi.javalearning.currencyexchange.util.InputValidator;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImpl.INSTANCE;
    private final ResponseJsonMapper jsonMapper = ResponseJsonMapperImpl.INSTANCE;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase("PATCH")) {
            // doPatch(request, response);  // TODO
        } else {
            super.service(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] codePair = InputValidator.getValidatedCodePair(request);
        String baseCurrencyCode = codePair[0];
        String targetCurrencyCode = codePair[1];
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByCodePair(baseCurrencyCode, targetCurrencyCode);
        jsonMapper.write(response.getWriter(), DtoMapper.toResponseDTO(exchangeRate));
    }
}
