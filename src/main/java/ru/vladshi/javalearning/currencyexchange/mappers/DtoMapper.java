package ru.vladshi.javalearning.currencyexchange.mappers;

import ru.vladshi.javalearning.currencyexchange.dto.CurrencyDto;
import ru.vladshi.javalearning.currencyexchange.models.Currency;

public class DtoMapper {

    private static final DtoMapper INSTANCE = new DtoMapper();

    private DtoMapper() {
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

    public static DtoMapper getInstance() {
        return INSTANCE;
    }
}
