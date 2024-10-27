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
import java.util.*;

public enum ExchangeRateDaoImpl implements ExchangeRateDao {

    INSTANCE;

    @Override
    public List<ExchangeRate> findAll() {
        final String query = """
                                SELECT er.id        AS id,
                                       bc.id        AS base_id,
                                       bc.code      AS base_code,
                                       bc.full_name AS base_full_name,
                                       bc.sign      AS base_sign,
                                       tc.id        AS target_id,
                                       tc.code      AS target_code,
                                       tc.full_name AS target_full_name,
                                       tc.sign      AS target_sign,
                                       er.rate      AS rate
                                FROM exchange_rate AS er
                                         JOIN currency AS bc ON bc.id = er.base_currency_id
                                         JOIN currency AS tc ON tc.id = er.target_currency_id;
        """;
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (
            Connection connection = ConnectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)
        ) {
            // TODO зарефакторить buildExchangeRate версию с двумя методами, так что бы Мар создавался внутри и
            //  переименовать во что-то типа fillExchangeRatesListFromResultSet...
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

    @Override
    public Optional<ExchangeRate> findByCodePair(String baseCurrencyCode, String targetCurrencyCode) {
        final String query = """
                                SELECT er.id        AS id,
                                       bc.id        AS base_id,
                                       bc.code      AS base_code,
                                       bc.full_name AS base_full_name,
                                       bc.sign      AS base_sign,
                                       tc.id        AS target_id,
                                       tc.code      AS target_code,
                                       tc.full_name AS target_full_name,
                                       tc.sign      AS target_sign,
                                       er.rate      AS rate
                                FROM exchange_rate AS er
                                    JOIN currency AS bc ON er.base_currency_id = bc.id
                                    JOIN currency AS tc ON er.target_currency_id = tc.id
                                WHERE base_code = ? AND target_code = ?;
        """;
        try (
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, baseCurrencyCode);
            statement.setString(2, targetCurrencyCode);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ExchangeRate exchangeRate = buildExchangeRate(resultSet);
                resultSet.close();
                return Optional.of(exchangeRate);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database is unavailable");
        }
        return Optional.empty();
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
