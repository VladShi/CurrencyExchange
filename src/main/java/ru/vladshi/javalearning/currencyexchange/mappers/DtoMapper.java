package ru.vladshi.javalearning.currencyexchange.mappers;

import ru.vladshi.javalearning.currencyexchange.dto.CurrencyDto;
import ru.vladshi.javalearning.currencyexchange.dto.ExchangeRateDto;
import ru.vladshi.javalearning.currencyexchange.models.Currency;
import ru.vladshi.javalearning.currencyexchange.models.ExchangeRate;

public class DtoMapper {

    public static CurrencyDto toDTO(Currency currency) {
        return new CurrencyDto(
                currency.getId(),
                currency.getCode(),
                currency.getFullName(),
                currency.getSign());
    }

    public static Currency toModel(CurrencyDto dto) {
        return new Currency(
                dto.getId(),
                dto.getCode(),
                dto.getFullName(),
                dto.getSign());
    }

    public static ExchangeRateDto toDTO(ExchangeRate exchangeRate) {
        return new ExchangeRateDto(
                exchangeRate.getId(),
                toDTO(exchangeRate.getBaseCurrency()),
                toDTO(exchangeRate.getTargetCurrency()),
                exchangeRate.getRate());
    }
}
