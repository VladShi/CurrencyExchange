package ru.vladshi.javalearning.currencyexchange.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vladshi.javalearning.currencyexchange.dto.CurrencyDto;
import ru.vladshi.javalearning.currencyexchange.mappers.DtoMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseJsonMapperImpl;
import ru.vladshi.javalearning.currencyexchange.services.CurrencyService;
import ru.vladshi.javalearning.currencyexchange.services.CurrencyServiceImpl;
import ru.vladshi.javalearning.currencyexchange.util.InputValidator;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyServiceImpl.INSTANCE;
    private final ResponseJsonMapper jsonMapper = ResponseJsonMapperImpl.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<CurrencyDto> allCurrencies =
                currencyService.getAllCurrencies()
                .stream()
                .map(DtoMapper::toResponseDTO)
                .toList();
        jsonMapper.write(response.getWriter(), allCurrencies);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CurrencyDto currencyDto = InputValidator.getValidatedCurrencyDto(request);
        int insertedId = currencyService.addCurrency(DtoMapper.toModel(currencyDto));
        currencyDto.setId(insertedId);
        jsonMapper.write(response.getWriter(), currencyDto);
        response.setStatus(HttpServletResponse.SC_CREATED);  // 201
    }
}