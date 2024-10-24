package ru.vladshi.javalearning.currencyexchange.dao;

import org.sqlite.SQLiteException;
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
import java.util.Optional;
import java.util.OptionalInt;

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
                currencies.add(buildCurrency(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database is unavailable");
        }
        return currencies;
    }

    @Override
    public OptionalInt addCurrency(Currency currency) {
        final String query = "INSERT INTO currency (code, full_name, sign) VALUES (?, ?, ?)";
        int insertedId;
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
                insertedId = generatedKeys.getInt(1);
            }
            else {
                generatedKeys.close();
                throw new DatabaseException("Adding new currency failed, no ID obtained.");
            }
            generatedKeys.close();
        } catch (SQLException e) {
            if (e instanceof SQLiteException && e.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                return OptionalInt.empty();
            }
            throw new DatabaseException("Database is unavailable");
        }
        return OptionalInt.of(insertedId);
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        final String query = "SELECT * FROM currency WHERE code = ?";
        try (
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Currency currency = buildCurrency(resultSet);
                resultSet.close();
                return Optional.of(currency);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database is unavailable");
        }
        return Optional.empty();
    }

    private Currency buildCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }
}
