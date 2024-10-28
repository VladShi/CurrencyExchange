package ru.vladshi.javalearning.currencyexchange.mappers;

import ru.vladshi.javalearning.currencyexchange.dto.CurrencyDto;
import ru.vladshi.javalearning.currencyexchange.dto.ExchangeRateRequestDto;
import ru.vladshi.javalearning.currencyexchange.dto.ExchangeRateResponseDto;
import ru.vladshi.javalearning.currencyexchange.dto.ExchangeResponseDto;
import ru.vladshi.javalearning.currencyexchange.models.Currency;
import ru.vladshi.javalearning.currencyexchange.models.ExchangeRate;

import java.math.BigDecimal;

public class DtoMapper {

    public static CurrencyDto toResponseDTO(Currency model) {
        return new CurrencyDto(
                model.getId(),
                model.getCode(),
                model.getFullName(),
                model.getSign());
    }

    public static Currency toModel(CurrencyDto dto) {
        return new Currency(
                dto.getId(),
                dto.getCode(),
                dto.getFullName(),
                dto.getSign());
    }

    public static ExchangeRateResponseDto toResponseDTO(ExchangeRate model) {
        return new ExchangeRateResponseDto(
                model.getId(),
                toResponseDTO(model.getBaseCurrency()),
                toResponseDTO(model.getTargetCurrency()),
                model.getRate());
    }

    public static ExchangeRate toModel(ExchangeRateRequestDto dto) {
        return new ExchangeRate(
                new Currency(dto.getBaseCurrencyCode()),
                new Currency(dto.getTargetCurrencyCode()),
                dto.getRate());
    }

    public static ExchangeResponseDto toExchangeResponseDTO(ExchangeRate exchangeRate,
                                                            BigDecimal amount,
                                                            BigDecimal convertedAmount) {
        return new ExchangeResponseDto(
                toResponseDTO(exchangeRate.getBaseCurrency()),
                toResponseDTO(exchangeRate.getTargetCurrency()),
                exchangeRate.getRate(),
                amount,
                convertedAmount
        );
    }
}
