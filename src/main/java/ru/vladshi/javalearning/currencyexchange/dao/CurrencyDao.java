package ru.vladshi.javalearning.currencyexchange.dao;

import ru.vladshi.javalearning.currencyexchange.models.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyDao {

    List<Currency> findAll();

    int addCurrency(Currency currency);

    Optional<Currency> findByCode(String code);
}
