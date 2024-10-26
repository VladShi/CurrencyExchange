package ru.vladshi.javalearning.currencyexchange.dao;

import ru.vladshi.javalearning.currencyexchange.models.Currency;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface CurrencyDao {

    List<Currency> findAll();

    OptionalInt save(Currency currency);

    Optional<Currency> findByCode(String code);
}
