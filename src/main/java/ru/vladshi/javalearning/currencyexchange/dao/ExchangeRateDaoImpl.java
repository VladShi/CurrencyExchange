package ru.vladshi.javalearning.currencyexchange.dao;

import org.sqlite.SQLiteException;
import ru.vladshi.javalearning.currencyexchange.exceptions.DatabaseException;
import ru.vladshi.javalearning.currencyexchange.models.Currency;
import ru.vladshi.javalearning.currencyexchange.models.ExchangeRate;
import ru.vladshi.javalearning.currencyexchange.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

public enum ExchangeRateDaoImpl implements ExchangeRateDao {

    INSTANCE;

    @Override
    public List<ExchangeRate> findAll() {
        final String query = """
                                SELECT er_t.id,
                                       base_t.id          as base_id,
                                       base_t.code        as base_code,
                                       base_t.full_name   as base_full_name,
                                       base_t.sign        as base_sign,
                                       target_t.id        as target_id,
                                       target_t.code      as target_code,
                                       target_t.full_name as target_full_name,
                                       target_t.sign      as target_sign,
                                       er_t.rate
                                FROM exchange_rate er_t
                                    JOIN currency base_t ON base_t.id = er_t.base_currency_id
                                    JOIN currency target_t ON target_t.id = er_t.target_currency_id;
        """;
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (
            Connection connection = ConnectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)
        ) {
            Map<Integer, Currency> tempCurrenciesHolder = new HashMap<>();
            while (resultSet.next()) {
                exchangeRates.add(buildExchangeRate(resultSet, tempCurrenciesHolder));
            }
            tempCurrenciesHolder.clear();
        } catch (SQLException e) {
            throw new DatabaseException("Database is unavailable");
        }
        return exchangeRates;
    }

    @Override
    public OptionalInt save(ExchangeRate exchangeRate) {
        final String query = "INSERT INTO exchange_rate (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)";
        int insertedId;
        try (
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ){
            statement.setInt(1, exchangeRate.getBaseCurrency().getId());
            statement.setInt(2, exchangeRate.getTargetCurrency().getId());
            statement.setBigDecimal(3, exchangeRate.getRate());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                insertedId = generatedKeys.getInt(1);
            }
            else {
                generatedKeys.close();
                throw new DatabaseException("Adding new exchange rate failed, no ID obtained.");
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

    private ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(
                resultSet.getInt("id"),
                buildBaseCurrency(resultSet),
                buildTargetCurrency(resultSet),
                resultSet.getBigDecimal("rate"));
    }

    private ExchangeRate buildExchangeRate(ResultSet resultSet, Map<Integer, Currency> tempCurrenciesHolder)
            throws SQLException {
        // tempCurrenciesHolder -  is needed to avoid creating and keeping in memory too many Currency instances that
        // will have identical field values.
        Integer baseId = resultSet.getInt("base_id");
        Integer targetId = resultSet.getInt("target_id");
        Currency baseCurrency;
        if (tempCurrenciesHolder.containsKey(baseId)) {
            baseCurrency = tempCurrenciesHolder.get(baseId);
        } else {
            baseCurrency = buildBaseCurrency(resultSet);
            tempCurrenciesHolder.put(baseId, baseCurrency);
        }
        Currency targetCurrency;
        if (tempCurrenciesHolder.containsKey(targetId)) {
            targetCurrency = tempCurrenciesHolder.get(targetId);
        } else {
            targetCurrency = buildTargetCurrency(resultSet);
            tempCurrenciesHolder.put(targetId, targetCurrency);
        }
        return new ExchangeRate(
                resultSet.getInt("id"),
                baseCurrency,
                targetCurrency,
                resultSet.getBigDecimal("rate"));
    }

    private static Currency buildBaseCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("base_id"),
                resultSet.getString("base_code"),
                resultSet.getString("base_full_name"),
                resultSet.getString("base_sign")
        );
    }

    private static Currency buildTargetCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("target_id"),
                resultSet.getString("target_code"),
                resultSet.getString("target_full_name"),
                resultSet.getString("target_sign")
        );
    }
}
