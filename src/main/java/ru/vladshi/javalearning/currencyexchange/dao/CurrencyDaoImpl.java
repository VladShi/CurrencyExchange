package ru.vladshi.javalearning.currencyexchange.dao;

import org.sqlite.SQLiteException;
import ru.vladshi.javalearning.currencyexchange.exceptions.DataExistsException;
import ru.vladshi.javalearning.currencyexchange.exceptions.DatabaseException;
import ru.vladshi.javalearning.currencyexchange.models.Currency;
import ru.vladshi.javalearning.currencyexchange.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public enum CurrencyDaoImpl implements CurrencyDao {

    INSTANCE;

    @Override
    public List<Currency> findAll() {
        final String query = "SELECT * FROM currency";
        List<Currency> currencies = new ArrayList<>();
        try (
            Connection connection = ConnectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)
        ) {
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

    @Override
    public int addCurrency(Currency currency) {
        final String query = "INSERT INTO currency (code, full_name, sign) VALUES (?, ?, ?)";
        int addedId;
        try (
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                addedId = generatedKeys.getInt(1);
            }
            else {
                generatedKeys.close();
                throw new DatabaseException("Adding new currency failed, no ID obtained.");
            }
            generatedKeys.close();
        } catch (SQLException e) {
            if (e instanceof SQLiteException && e.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                throw new DataExistsException("This currency already exists");
            }
            throw new RuntimeException(e);
        }
        return addedId;
    }
}
