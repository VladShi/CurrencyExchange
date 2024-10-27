package ru.vladshi.javalearning.currencyexchange.services;

import ru.vladshi.javalearning.currencyexchange.models.ExchangeRate;

import java.util.List;

public interface ExchangeRateService {

    List<ExchangeRate> getAllExchangeRates();

    ExchangeRate addExchangeRate(ExchangeRate model);

    ExchangeRate getExchangeRateByCodePair(String baseCurrencyCode, String targetCurrencyCode);
}
