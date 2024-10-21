package ru.vladshi.javalearning.currencyexchange.dao;

import ru.vladshi.javalearning.currencyexchange.models.Currency;

import java.util.List;

public interface CurrencyDao {

    List<Currency> findAll();
}
