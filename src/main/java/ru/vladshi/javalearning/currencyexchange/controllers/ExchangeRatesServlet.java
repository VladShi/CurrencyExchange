package ru.vladshi.javalearning.currencyexchange.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vladshi.javalearning.currencyexchange.dto.ExchangeRateRequestDto;
import ru.vladshi.javalearning.currencyexchange.dto.ExchangeRateResponseDto;
import ru.vladshi.javalearning.currencyexchange.mappers.DtoMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapperImpl;
import ru.vladshi.javalearning.currencyexchange.models.ExchangeRate;
import ru.vladshi.javalearning.currencyexchange.services.ExchangeRateService;
import ru.vladshi.javalearning.currencyexchange.services.ExchangeRateServiceImpl;
import ru.vladshi.javalearning.currencyexchange.util.InputValidator;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet  extends HttpServlet {

    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImpl.INSTANCE;
    private final ResponseJsonMapper jsonMapper = ResponseJsonMapperImpl.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<ExchangeRateResponseDto> exchangeRates =
                exchangeRateService.getAllExchangeRates()
                .stream()
                .map(DtoMapper::toResponseDTO)
                .toList();
        jsonMapper.write(response.getWriter(), exchangeRates);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExchangeRateRequestDto exchangeRateDto = InputValidator.getValidatedExchangeRateRequestDto(request);
        ExchangeRate exchangeRate = exchangeRateService.addExchangeRate(DtoMapper.toModel(exchangeRateDto));
        jsonMapper.write(response.getWriter(), DtoMapper.toResponseDTO(exchangeRate));
        response.setStatus(HttpServletResponse.SC_CREATED);  // 201
    }
}