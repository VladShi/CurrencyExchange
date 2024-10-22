package ru.vladshi.javalearning.currencyexchange.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vladshi.javalearning.currencyexchange.dto.CurrencyDto;
import ru.vladshi.javalearning.currencyexchange.mappers.DtoMapper;
import ru.vladshi.javalearning.currencyexchange.mappers.ResponseMapper;
import ru.vladshi.javalearning.currencyexchange.services.CurrencyService;
import ru.vladshi.javalearning.currencyexchange.services.CurrencyServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyServiceImpl.getInstance();
    private final ResponseMapper jsonMapper = ResponseMapper.getInstance();
    private final DtoMapper dtoMapper = DtoMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        List<CurrencyDto> allCurrencies = currencyService.getAllCurrencies().stream().map(dtoMapper::toDTO).toList();
        jsonMapper.writeToResponse(response, allCurrencies);
    }
}
