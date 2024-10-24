package ru.vladshi.javalearning.currencyexchange.services;

import ru.vladshi.javalearning.currencyexchange.dao.ExchangeRateDao;
import ru.vladshi.javalearning.currencyexchange.dao.ExchangeRateDaoImpl;
import ru.vladshi.javalearning.currencyexchange.exceptions.DataNotFoundException;
import ru.vladshi.javalearning.currencyexchange.models.ExchangeRate;

import java.util.List;

public enum ExchangeRateServiceImpl implements ExchangeRateService {

    INSTANCE;

    private final ExchangeRateDao exchangeRateDao = ExchangeRateDaoImpl.INSTANCE;

    @Override
    public List<ExchangeRate> getAllExchangeRates() {
        List<ExchangeRate> exchangeRates = exchangeRateDao.findAll();
        if (exchangeRates.isEmpty()) {
            throw new DataNotFoundException("There are no exchange rates yet");
        }
        return exchangeRates;
    }
}
