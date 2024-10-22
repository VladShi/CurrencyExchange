package ru.vladshi.javalearning.currencyexchange.dao;

import ru.vladshi.javalearning.currencyexchange.exceptions.DatabaseException;
import ru.vladshi.javalearning.currencyexchange.models.Currency;
import ru.vladshi.javalearning.currencyexchange.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public enum CurrencyDaoImpl implements CurrencyDao {

    INSTANCE;

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
            throw new DatabaseException("Database is unavailable");
        }
        return currencies;
    }
}
