package ru.vladshi.javalearning.currencyexchange.services;

import ru.vladshi.javalearning.currencyexchange.dao.CurrencyDao;
import ru.vladshi.javalearning.currencyexchange.dao.CurrencyDaoImpl;
import ru.vladshi.javalearning.currencyexchange.models.Currency;

import java.util.List;

public class CurrencyServiceImpl implements CurrencyService {

    private static final CurrencyServiceImpl INSTANCE = new CurrencyServiceImpl();
    private final CurrencyDao currencyDao = CurrencyDaoImpl.getInstance();

    private CurrencyServiceImpl() {
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyDao.findAll();
    }

    public static CurrencyServiceImpl getInstance() {
        return INSTANCE;
    }
}
