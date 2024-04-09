package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CurrencyValues;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.DataBase.SPEND;
import static guru.qa.niffler.data.jdbc.DataSourceContext.dataSource;

public class SpendRepositoryJdbc implements SpendRepository {

    private final DataSource spendDs = dataSource(SPEND);

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        try (Connection conn = spendDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement(
                     "INSERT INTO \"spend\" (username, spend_date, currency, amount, description, category_id)" +
                             " VALUES (?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            usersPs.setString(1, spend.getUsername());
            usersPs.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
            usersPs.setString(3, spend.getCurrency().name());
            usersPs.setDouble(4, spend.getAmount());
            usersPs.setString(5, spend.getDescription());
            usersPs.setObject(6, spend.getCategory().getId());
            usersPs.executeUpdate();
            UUID generatedUserId;
            try (ResultSet generatedKeys = usersPs.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedUserId = UUID.fromString(generatedKeys.getString("id"));
                } else {
                    throw new IllegalStateException("Can`t obtain id from given ResultSet");
                }
            }
            spend.setId(generatedUserId);
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        SpendEntity spend = new SpendEntity();
        try (Connection conn = spendDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM \"spend\" WHERE id = ? ")) {
            usersPs.setObject(1, id);

            usersPs.execute();
            ResultSet resultSet = usersPs.getResultSet();

            if (resultSet.next()) {
                spend.setId(resultSet.getObject("id", UUID.class));
                spend.setUsername(resultSet.getString("username"));
                spend.setSpendDate(resultSet.getDate("spend_date"));
                spend.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                spend.setAmount(resultSet.getDouble("amount"));
                spend.setDescription(resultSet.getString("description"));
                spend.setCategory(
                        findCategoryById(
                                resultSet.getObject("category_id", UUID.class)
                        ).orElseThrow()
                );
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(spend);
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        try (Connection conn = spendDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement(
                     "INSERT INTO \"category\" (category, username)" +
                             " VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            usersPs.setString(1, category.getCategory());
            usersPs.setString(2, category.getUsername());
            usersPs.executeUpdate();
            UUID generatedUserId;
            try (ResultSet generatedKeys = usersPs.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedUserId = UUID.fromString(generatedKeys.getString("id"));
                } else {
                    throw new IllegalStateException("Can`t obtain id from given ResultSet");
                }
            }
            category.setId(generatedUserId);
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        CategoryEntity category = new CategoryEntity();
        try (Connection conn = spendDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM \"category\" WHERE id = ? ")) {
            usersPs.setObject(1, id);

            usersPs.execute();
            ResultSet resultSet = usersPs.getResultSet();

            if (resultSet.next()) {
                category.setId(resultSet.getObject("id", UUID.class));
                category.setUsername(resultSet.getString("username"));
                category.setCategory(resultSet.getString("category"));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(category);
    }

    @Override
    public Optional<CategoryEntity> findUserCategoryByName(String username, String category) {
        CategoryEntity result = new CategoryEntity();
        try (Connection conn = spendDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM \"category\" WHERE \"category\".category = ? and username = ?")) {
            usersPs.setString(1, category);
            usersPs.setString(2, username);

            usersPs.execute();
            ResultSet resultSet = usersPs.getResultSet();

            if (resultSet.next()) {
                result.setId(resultSet.getObject("id", UUID.class));
                result.setUsername(resultSet.getString("username"));
                result.setCategory(resultSet.getString("category"));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(result);
    }
}
