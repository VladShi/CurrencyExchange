package ru.vladshi.javalearning.currencyexchange.services;

import ru.vladshi.javalearning.currencyexchange.dao.CurrencyDao;
import ru.vladshi.javalearning.currencyexchange.dao.CurrencyDaoImpl;
import ru.vladshi.javalearning.currencyexchange.exceptions.DataNotFoundException;
import ru.vladshi.javalearning.currencyexchange.models.Currency;

import java.util.List;
import java.util.Optional;

public enum CurrencyServiceImpl implements CurrencyService {

    INSTANCE;

    private final CurrencyDao currencyDao = CurrencyDaoImpl.INSTANCE;

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyDao.findAll();
    }

    @Override
    public int addCurrency(Currency currency) {
        return currencyDao.addCurrency(currency);
    }

    @Override
    public Currency getCurrencyByCode(String currencyCode) {
        Optional<Currency> currency = currencyDao.findByCode(currencyCode);
        return currency.orElseThrow(() -> new DataNotFoundException("Currency not found"));
    }
}
