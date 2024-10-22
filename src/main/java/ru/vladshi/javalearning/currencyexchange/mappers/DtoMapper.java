package ru.vladshi.javalearning.currencyexchange.mappers;

import ru.vladshi.javalearning.currencyexchange.dto.CurrencyDto;
import ru.vladshi.javalearning.currencyexchange.models.Currency;

public enum DtoMapper {

    INSTANCE;

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
}
