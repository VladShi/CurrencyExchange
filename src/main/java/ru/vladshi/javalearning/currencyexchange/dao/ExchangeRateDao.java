package ru.vladshi.javalearning.currencyexchange.dao;

import ru.vladshi.javalearning.currencyexchange.models.ExchangeRate;

import java.util.List;

public interface ExchangeRateDao {

    List<ExchangeRate> findAll();
}
