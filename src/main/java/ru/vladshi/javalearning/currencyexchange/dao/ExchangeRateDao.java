package ru.vladshi.javalearning.currencyexchange.dao;

import ru.vladshi.javalearning.currencyexchange.models.ExchangeRate;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface ExchangeRateDao {

    List<ExchangeRate> findAll();

    OptionalInt save(ExchangeRate exchangeRate);

    Optional<ExchangeRate> findByCodePair(String baseCurrencyCode, String targetCurrencyCode);
}
