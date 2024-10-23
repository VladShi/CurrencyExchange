package ru.vladshi.javalearning.currencyexchange.util;

import jakarta.servlet.http.HttpServletRequest;
import ru.vladshi.javalearning.currencyexchange.dto.CurrencyDto;
import ru.vladshi.javalearning.currencyexchange.exceptions.InvalidDataException;

public class FormValidator {

    public static CurrencyDto getValidatedCurrencyDto(HttpServletRequest request) {
        String code = request.getParameter("code");
        checkCode(code);
        String name = request.getParameter("name");
        checkName(name);
        String sign = request.getParameter("sign");
        checkSign(sign);
        return new CurrencyDto(code.toUpperCase(), name, sign);
    }

    private static void checkCode(String code) {
        if (code == null || !code.matches("^[a-zA-Z]{3}$")) {
            throw new InvalidDataException("Currency code is incorrect");
        }
    }

    private static void checkName(String name) {
        if (name == null || name.isBlank() || name.length() > 30) {
            throw new InvalidDataException("Name is incorrect");
        }
    }

    private static void checkSign(String sign) {
        if (sign == null || sign.isBlank() || sign.length() > 2) {
            throw new InvalidDataException("Sign is incorrect");
        }
    }
}
