package ru.vladshi.javalearning.currencyexchange.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vladshi.javalearning.currencyexchange.dto.ExchangeRateDto;
import ru.vladshi.javalearning.currencyexchange.mappers.DtoMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapperImpl;
import ru.vladshi.javalearning.currencyexchange.services.ExchangeRateService;
import ru.vladshi.javalearning.currencyexchange.services.ExchangeRateServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet  extends HttpServlet {

    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImpl.INSTANCE;
    private final ResponseJsonMapper jsonMapper = ResponseJsonMapperImpl.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<ExchangeRateDto> exchangeRates =
                exchangeRateService.getAllExchangeRates()
                .stream()
                .map(DtoMapper::toDTO)
                .toList();
        jsonMapper.writeToResponse(response, exchangeRates);
    }
}