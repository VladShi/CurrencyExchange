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

    public CurrencyDto(String code, String fullName, String sign) {
        this.id = null;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
