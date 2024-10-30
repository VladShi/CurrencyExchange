package ru.vladshi.javalearning.currencyexchange.util;

import jakarta.servlet.http.HttpServletRequest;
import ru.vladshi.javalearning.currencyexchange.dto.CurrencyDto;
import ru.vladshi.javalearning.currencyexchange.dto.ExchangeRateRequestDto;
import ru.vladshi.javalearning.currencyexchange.dto.ExchangeRequestDto;
import ru.vladshi.javalearning.currencyexchange.exceptions.InvalidDataException;

import java.io.IOException;
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
        BigDecimal rate = checkAndGetAsBigDecimal(rateAsString, "Rate");
        return new ExchangeRateRequestDto(baseCurrencyCode.toUpperCase(), targetCurrencyCode.toUpperCase(), rate);
    }

    public static BigDecimal getValidatedRateFromPatchRequest(HttpServletRequest request) throws IOException {
        String rateAsString = "";
        String requestBody = request.getReader().readLine();
        if (requestBody != null && !requestBody.isBlank()) {
            String[] parameters = requestBody.split("&");
            for (String parameter : parameters) {
                String[] keyValue = parameter.split("=");
                String key = keyValue[0];
                String value = keyValue[1];
                if (key.equals("rate")) {
                    rateAsString = value;
                }
            }
        }
        return checkAndGetAsBigDecimal(rateAsString, "Rate");
    }

    public static ExchangeRequestDto getValidatedExchangeRequestDto(HttpServletRequest request) {
        String baseCurrencyCode = request.getParameter("from");
        checkCode(baseCurrencyCode);
        String targetCurrencyCode = request.getParameter("to");
        checkCode(targetCurrencyCode);
        String amountAsString = request.getParameter("amount");
        BigDecimal amount = checkAndGetAsBigDecimal(amountAsString, "Amount");
        return new ExchangeRequestDto(baseCurrencyCode, targetCurrencyCode, amount);
    }

    private static void checkCode(String code) {
        checkCode(code, "Currency code");
    }

    private static void checkCode(String code, String parameterName) {
        if (code == null || !isThreeLatinLetters(code)) {
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

    private static BigDecimal checkAndGetAsBigDecimal(String parameterValue, String parameterName) {
        BigDecimal rateAsBigDecimal;
        if (parameterValue == null || parameterValue.isBlank()) {
            throw new InvalidDataException(parameterName + " field is required");
        }
        parameterValue = parameterValue.replace(',', '.');
        if (parameterValue.length() > 20 || !isPositiveNumericWithSixDecimalMaximum(parameterValue)) {
            throw new InvalidDataException(parameterName + " is incorrect");
        }
        rateAsBigDecimal = new BigDecimal(parameterValue);
        if (rateAsBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidDataException(parameterName + " must not be equal to zero");
        }
        return rateAsBigDecimal;
    }

    private static boolean isPositiveNumericWithSixDecimalMaximum(String input) {
        return input.matches("^[0-9]*([.][0-9]{0,6})?$");
    }

    private static boolean isThreeLatinLetters (String input) {
        return input.matches("^[a-zA-Z]{3}$");
    }
}
