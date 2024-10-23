package ru.vladshi.javalearning.currencyexchange.services;

import ru.vladshi.javalearning.currencyexchange.dao.CurrencyDao;
import ru.vladshi.javalearning.currencyexchange.dao.CurrencyDaoImpl;
import ru.vladshi.javalearning.currencyexchange.models.Currency;

import java.util.List;

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
}
