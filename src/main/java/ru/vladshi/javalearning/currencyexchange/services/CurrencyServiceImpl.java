package ru.vladshi.javalearning.currencyexchange.services;

import ru.vladshi.javalearning.currencyexchange.dao.CurrencyDao;
import ru.vladshi.javalearning.currencyexchange.dao.CurrencyDaoImpl;
import ru.vladshi.javalearning.currencyexchange.exceptions.DataExistsException;
import ru.vladshi.javalearning.currencyexchange.exceptions.DataNotFoundException;
import ru.vladshi.javalearning.currencyexchange.models.Currency;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public enum CurrencyServiceImpl implements CurrencyService {

    INSTANCE;

    private final CurrencyDao currencyDao = CurrencyDaoImpl.INSTANCE;

    @Override
    public List<Currency> getAllCurrencies() {
        List<Currency> currencies = currencyDao.findAll();
        if (currencies.isEmpty()) {
            throw new DataNotFoundException("There are no currencies yet");
        }
        return currencies;
    }

    @Override
    public int addCurrency(Currency currency) {
        OptionalInt insertedId = currencyDao.save(currency);
        return insertedId.orElseThrow(() -> new DataExistsException("This currency already exists"));
    }

    @Override
    public Currency getCurrencyByCode(String currencyCode) {
        Optional<Currency> currency = currencyDao.findByCode(currencyCode);
        return currency.orElseThrow(() -> new DataNotFoundException("Currency " + currencyCode + " not found"));
    }
}
