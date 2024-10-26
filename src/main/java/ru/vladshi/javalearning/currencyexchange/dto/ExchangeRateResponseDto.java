package ru.vladshi.javalearning.currencyexchange.dto;

import java.math.BigDecimal;

public class ExchangeRateResponseDto {
    private Integer id;
    private CurrencyDto baseCurrency;
    private CurrencyDto targetCurrency;
    private BigDecimal rate;

    public ExchangeRateResponseDto() {
    }

    public ExchangeRateResponseDto(Integer id, CurrencyDto baseCurrency, CurrencyDto targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CurrencyDto getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyDto baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyDto getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(CurrencyDto targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
