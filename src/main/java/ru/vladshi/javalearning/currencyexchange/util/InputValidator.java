package ru.vladshi.javalearning.currencyexchange.util;

import jakarta.servlet.http.HttpServletRequest;
import ru.vladshi.javalearning.currencyexchange.dto.CurrencyDto;
import ru.vladshi.javalearning.currencyexchange.exceptions.InvalidDataException;

public class InputValidator {

    public static CurrencyDto getValidatedCurrencyDto(HttpServletRequest request) {
        String code = request.getParameter("code");
        checkCode(code);
        String name = request.getParameter("name");
        checkName(name);
        String sign = request.getParameter("sign");
        checkSign(sign);
        return new CurrencyDto(code.toUpperCase(), name, sign);
    }

    public static String getValidatedCurrencyCode(HttpServletRequest request) {
        String path = request.getPathInfo();
        if (path == null || path.length() <= 1) {
            throw new InvalidDataException("Currency code is incorrect");
        }
        String currencyCode = path.substring(1);
        checkCode(currencyCode);
        return currencyCode.toUpperCase();
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
