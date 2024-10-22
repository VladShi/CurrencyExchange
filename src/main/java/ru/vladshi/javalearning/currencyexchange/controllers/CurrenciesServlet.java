package ru.vladshi.javalearning.currencyexchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vladshi.javalearning.currencyexchange.dto.CurrencyDto;
import ru.vladshi.javalearning.currencyexchange.dto.mappers.Mapper;
import ru.vladshi.javalearning.currencyexchange.services.CurrencyService;
import ru.vladshi.javalearning.currencyexchange.services.CurrencyServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyServiceImpl.getInstance();
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final Mapper dtoMapper = Mapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        List<CurrencyDto> allCurrencies = currencyService.getAllCurrencies().stream().map(dtoMapper::toDTO).toList();
        String jsonResponse = jsonMapper.writeValueAsString(allCurrencies);
        response.getWriter().write(jsonResponse);
    }
}
