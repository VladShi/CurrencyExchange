package ru.vladshi.javalearning.currencyexchange.services;

import ru.vladshi.javalearning.currencyexchange.dao.ExchangeRateDao;
import ru.vladshi.javalearning.currencyexchange.dao.ExchangeRateDaoImpl;
import ru.vladshi.javalearning.currencyexchange.exceptions.DataExistsException;
import ru.vladshi.javalearning.currencyexchange.exceptions.DataNotFoundException;
import ru.vladshi.javalearning.currencyexchange.models.Currency;
import ru.vladshi.javalearning.currencyexchange.models.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static java.math.RoundingMode.HALF_DOWN;

public enum ExchangeRateServiceImpl implements ExchangeRateService {

    INSTANCE;

    private final ExchangeRateDao exchangeRateDao = ExchangeRateDaoImpl.INSTANCE;
    private final CurrencyService currencyService = CurrencyServiceImpl.INSTANCE;
    private static final String CROSS_RATE_CURRENCY_CODE = "USD";

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

    @Override
    public ExchangeRate getExchangeRateByCodePair(String baseCurrencyCode, String targetCurrencyCode) {
        Optional<ExchangeRate> exchangeRate = exchangeRateDao.findByCodePair(baseCurrencyCode, targetCurrencyCode);
        return exchangeRate.orElseThrow(() -> new DataNotFoundException(
                "Exchange rate %s to %s not found".formatted(baseCurrencyCode, targetCurrencyCode)));
    }

    @Override
    public ExchangeRate updateExchangeRateByCodePair(String baseCurrencyCode,
                                                     String targetCurrencyCode,
                                                     BigDecimal rate) {
        ExchangeRate exchangeRate = getExchangeRateByCodePair(baseCurrencyCode, targetCurrencyCode);
        exchangeRate.setRate(rate);
        exchangeRateDao.update(exchangeRate);
        return exchangeRate;
    }

    @Override
    public BigDecimal getConvertedAmount(BigDecimal rate, BigDecimal amount) {
        return rate.multiply(amount).setScale(2, HALF_DOWN);
    }

    @Override
    public ExchangeRate findSuitableExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        String base = baseCurrencyCode;
        String target = targetCurrencyCode;

        Optional<ExchangeRate> exchangeRate = findByDirectExchange(base, target);
        if (exchangeRate.isEmpty()) {
            exchangeRate = findByReverseExchange(base, target);
        }
        if (exchangeRate.isEmpty()) {
            exchangeRate = findByCrossRate(base, target);
        }
        return exchangeRate.orElseThrow(() -> new DataNotFoundException(
                "Exchange rate %s to %s not found".formatted(baseCurrencyCode, targetCurrencyCode)));
    }

    private Optional<ExchangeRate> findByDirectExchange(String baseCurrencyCode, String targetCurrencyCode) {
        return exchangeRateDao.findByCodePair(baseCurrencyCode, targetCurrencyCode);
    }

    private Optional<ExchangeRate> findByReverseExchange(String baseCurrencyCode, String targetCurrencyCode) {
        Optional<ExchangeRate> exchangeRate = exchangeRateDao.findByCodePair(targetCurrencyCode, baseCurrencyCode);
        if (exchangeRate.isPresent()) {
            exchangeRate = reverseExchangeRate(exchangeRate.get());
        }
        return exchangeRate;
    }

    private Optional<ExchangeRate> reverseExchangeRate(ExchangeRate exchangeRate) {
        ExchangeRate reversedExchangeRate = new ExchangeRate();
        reversedExchangeRate.setBaseCurrency(exchangeRate.getTargetCurrency());
        reversedExchangeRate.setTargetCurrency(exchangeRate.getBaseCurrency());
        BigDecimal reversedRate = BigDecimal.valueOf(1L).divide(exchangeRate.getRate(),6, HALF_DOWN);
        reversedExchangeRate.setRate(reversedRate);
        return Optional.of(reversedExchangeRate);
    }

    private Optional<ExchangeRate> findByCrossRate(String baseCurrencyCode, String targetCurrencyCode) {
        Optional<ExchangeRate> baseCrossRate = exchangeRateDao.findByCodePair(
                                                                        CROSS_RATE_CURRENCY_CODE, baseCurrencyCode);
        Optional<ExchangeRate> targetCrossRate = exchangeRateDao.findByCodePair(
                                                                        CROSS_RATE_CURRENCY_CODE, targetCurrencyCode);
        if (baseCrossRate.isEmpty() || targetCrossRate.isEmpty()) {
            return Optional.empty();
        }
        BigDecimal resultRate = targetCrossRate.get().getRate()
                                    .divide(baseCrossRate.get().getRate(), 6, HALF_DOWN);
        return Optional.of(new ExchangeRate(
                baseCrossRate.get().getTargetCurrency(),
                targetCrossRate.get().getTargetCurrency(),
                resultRate
        ));
    }
}
