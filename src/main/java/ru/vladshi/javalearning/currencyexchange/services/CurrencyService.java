package ru.vladshi.javalearning.currencyexchange.services;

import ru.vladshi.javalearning.currencyexchange.models.Currency;

import java.util.List;

public interface CurrencyService {

    List<Currency> getAllCurrencies();
}
