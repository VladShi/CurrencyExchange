package ru.vladshi.javalearning.currencyexchange.services;

import ru.vladshi.javalearning.currencyexchange.dao.ExchangeRateDao;
import ru.vladshi.javalearning.currencyexchange.dao.ExchangeRateDaoImpl;
import ru.vladshi.javalearning.currencyexchange.exceptions.DataExistsException;
import ru.vladshi.javalearning.currencyexchange.exceptions.DataNotFoundException;
import ru.vladshi.javalearning.currencyexchange.models.Currency;
import ru.vladshi.javalearning.currencyexchange.models.ExchangeRate;

import java.util.List;
import java.util.OptionalInt;

public enum ExchangeRateServiceImpl implements ExchangeRateService {

    INSTANCE;

    private final ExchangeRateDao exchangeRateDao = ExchangeRateDaoImpl.INSTANCE;
    private final CurrencyService currencyService = CurrencyServiceImpl.INSTANCE;

    @Override
    public List<ExchangeRate> getAllExchangeRates() {
        List<ExchangeRate> exchangeRates = exchangeRateDao.findAll();
        if (exchangeRates.isEmpty()) {
            throw new DataNotFoundException("There are no exchange rates yet");
        }
        return exchangeRates;
    }

    @Override
    public ExchangeRate addExchangeRate(ExchangeRate exchangeRate) {
        String inputBaseCurrencyCode = exchangeRate.getBaseCurrency().getCode();
        Currency baseCurrency = currencyService.getCurrencyByCode(inputBaseCurrencyCode);
        exchangeRate.setBaseCurrency(baseCurrency);

        String inputTargetCurrencyCode = exchangeRate.getTargetCurrency().getCode();
        Currency targetCurrency = currencyService.getCurrencyByCode(inputTargetCurrencyCode);
        exchangeRate.setTargetCurrency(targetCurrency);

        OptionalInt insertedId = exchangeRateDao.save(exchangeRate);
        exchangeRate.setId(insertedId.orElseThrow(() -> new DataExistsException("This exchange rate already exists")));
        return exchangeRate;
    }
}
