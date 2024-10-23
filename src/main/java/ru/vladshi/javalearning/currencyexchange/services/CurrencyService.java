package ru.vladshi.javalearning.currencyexchange.services;

import ru.vladshi.javalearning.currencyexchange.models.Currency;

import java.util.List;

public interface CurrencyService {

    List<Currency> getAllCurrencies();

    int addCurrency(Currency currency);

    Currency getCurrencyByCode(String currencyCode);
}
