package ru.vladshi.javalearning.currencyexchange.util;

import jakarta.servlet.http.HttpServletRequest;
import ru.vladshi.javalearning.currencyexchange.dto.CurrencyDto;
import ru.vladshi.javalearning.currencyexchange.dto.ExchangeRateRequestDto;
import ru.vladshi.javalearning.currencyexchange.exceptions.InvalidDataException;

import java.math.BigDecimal;

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

    public static String[] getValidatedCodePair(HttpServletRequest request) {
        String path = request.getPathInfo();
        if (path == null || path.length() != 7) {
            throw new InvalidDataException("Pair of currency codes is incorrect");
        }
        String baseCurrencyCode = path.substring(1, 4);
        checkCode(baseCurrencyCode);
        String targetCurrencyCode = path.substring(4);
        checkCode(targetCurrencyCode);
        return new String[]{baseCurrencyCode.toUpperCase(), targetCurrencyCode.toUpperCase()};
    }

    public static ExchangeRateRequestDto getValidatedExchangeRateRequestDto(HttpServletRequest request) {
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        checkCode(baseCurrencyCode, "Base currency code");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        checkCode(targetCurrencyCode,  "Target currency code");
        String rateAsString = request.getParameter("rate");
        BigDecimal rate = checkAndConvertRate(rateAsString);
        return new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rate);
    }

    private static void checkCode(String code) {
        checkCode(code, "Currency code");
    }

    private static void checkCode(String code, String parameterName) {
        if (code == null || !code.matches("^[a-zA-Z]{3}$")) {
            throw new InvalidDataException(parameterName + " is incorrect");
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

    private static BigDecimal checkAndConvertRate(String rate) {
        BigDecimal rateAsBigDecimal;
        if (rate == null || rate.isBlank()) {
            throw new InvalidDataException("Rate field is required");
        }
        try {
            rateAsBigDecimal = new BigDecimal(rate);
        } catch (NumberFormatException e) {
            throw new InvalidDataException("Rate is incorrect");
        }
        return rateAsBigDecimal;
    }
}
