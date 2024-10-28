package ru.vladshi.javalearning.currencyexchange.services;

import ru.vladshi.javalearning.currencyexchange.models.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeRateService {

    List<ExchangeRate> getAllExchangeRates();

    ExchangeRate addExchangeRate(ExchangeRate model);

    ExchangeRate getExchangeRateByCodePair(String baseCurrencyCode, String targetCurrencyCode);

    ExchangeRate updateExchangeRateByCodePair(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);

    ExchangeRate findSuitableExchangeRate(String baseCurrencyCode, String targetCurrencyCode);

    BigDecimal getConvertedAmount(BigDecimal rate, BigDecimal amount);
}
