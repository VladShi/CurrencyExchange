package ru.vladshi.javalearning.currencyexchange.dao;

import ru.vladshi.javalearning.currencyexchange.models.Currency;
import ru.vladshi.javalearning.currencyexchange.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDaoImpl implements CurrencyDao {

    private static final CurrencyDaoImpl INSTANCE = new CurrencyDaoImpl();

    private CurrencyDaoImpl() {
    }

    @Override
    public List<Currency> findAll() {
        List<Currency> currencies = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM currency")) {
            while (resultSet.next()) {
                Currency currency = new Currency(
                    resultSet.getInt("id"),
                    resultSet.getString("code"),
                    resultSet.getString("full_name"),
                    resultSet.getString("sign")
                );
                currencies.add(currency);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return currencies;
    }

    public static CurrencyDaoImpl getInstance() {
        return INSTANCE;
    }
}
