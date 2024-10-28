package ru.vladshi.javalearning.currencyexchange.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vladshi.javalearning.currencyexchange.dto.ExchangeRequestDto;
import ru.vladshi.javalearning.currencyexchange.mappers.DtoMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapperImpl;
import ru.vladshi.javalearning.currencyexchange.models.ExchangeRate;
import ru.vladshi.javalearning.currencyexchange.services.ExchangeRateService;
import ru.vladshi.javalearning.currencyexchange.services.ExchangeRateServiceImpl;
import ru.vladshi.javalearning.currencyexchange.util.InputValidator;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImpl.INSTANCE;
    private final ResponseJsonMapper jsonMapper = ResponseJsonMapperImpl.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExchangeRequestDto exchangeRequestDto = InputValidator.getValidatedExchangeRequestDto(request);

        String baseCode = exchangeRequestDto.getBaseCurrencyCode();
        String targetCode = exchangeRequestDto.getTargetCurrencyCode();
        BigDecimal amount = exchangeRequestDto.getAmount();

        ExchangeRate exchangeRate = exchangeRateService.findSuitableExchangeRate(baseCode, targetCode);
        BigDecimal convertedAmount = exchangeRateService.getConvertedAmount(exchangeRate.getRate(), amount);
        jsonMapper.write(response.getWriter(), DtoMapper.toExchangeResponseDTO(exchangeRate, amount, convertedAmount));
    }
}
