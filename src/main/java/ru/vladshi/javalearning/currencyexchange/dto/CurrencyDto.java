package ru.vladshi.javalearning.currencyexchange.dto;

public class CurrencyDto {

    private Integer id;
    private String code;
    private String fullName;
    private String sign;

    public CurrencyDto() {
    }

    public CurrencyDto(Integer id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSign() {
        return sign;
    }
}
