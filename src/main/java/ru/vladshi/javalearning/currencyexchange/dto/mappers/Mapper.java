package ru.vladshi.javalearning.currencyexchange.dto.mappers;

import ru.vladshi.javalearning.currencyexchange.dto.CurrencyDto;
import ru.vladshi.javalearning.currencyexchange.models.Currency;

public class Mapper {

    private static final Mapper INSTANCE = new Mapper();

    private Mapper() {
    }

    public CurrencyDto toDTO(Currency currency) {
        return new CurrencyDto(
                currency.getId(),
                currency.getCode(),
                currency.getFullName(),
                currency.getSign());
    }

    public Currency toModel(CurrencyDto dto) {
        return new Currency(
                dto.getId(),
                dto.getCode(),
                dto.getFullName(),
                dto.getSign());
    }

    public static Mapper getInstance() {
        return INSTANCE;
    }
}
