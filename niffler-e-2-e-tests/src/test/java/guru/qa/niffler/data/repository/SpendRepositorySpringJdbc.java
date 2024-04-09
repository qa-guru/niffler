package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CurrencyValues;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.sjdbc.CategoryEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.DataBase.SPEND;
import static guru.qa.niffler.data.jdbc.DataSourceContext.dataSource;

public class SpendRepositorySpringJdbc implements SpendRepository {

    private final JdbcTemplate spendJdbcTemplate = new JdbcTemplate(dataSource(SPEND));

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        KeyHolder kh = new GeneratedKeyHolder();

        spendJdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"spend\" (username, spend_date, currency, amount, description, category_id)" +
                            " VALUES (?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, spend.getUsername());
            ps.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());
            return ps;
        }, kh);
        final UUID generatedUserId = (UUID) kh.getKeys().get("id");
        spend.setId(generatedUserId);
        return spend;
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        return Optional.ofNullable(
                spendJdbcTemplate.queryForObject(
                        "SELECT * FROM \"spend\" WHERE id = ? ",
                        (rs, rowNum) -> {
                            SpendEntity spend = new SpendEntity();
                            spend.setId(rs.getObject("id", UUID.class));
                            spend.setUsername(rs.getString("username"));
                            spend.setSpendDate(rs.getDate("spend_date"));
                            spend.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                            spend.setAmount(rs.getDouble("amount"));
                            spend.setDescription(rs.getString("description"));
                            spend.setCategory(
                                    findCategoryById(
                                            rs.getObject("category_id", UUID.class)
                                    ).orElseThrow()
                            );
                            return spend;
                        }, id
                )
        );
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        KeyHolder kh = new GeneratedKeyHolder();

        spendJdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"category\" (category, username)" +
                            " VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());
            return ps;
        }, kh);
        final UUID generatedUserId = (UUID) kh.getKeys().get("id");
        category.setId(generatedUserId);
        return category;
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return Optional.ofNullable(
                spendJdbcTemplate.queryForObject(
                        "SELECT * FROM \"category\" WHERE id = ? ",
                        CategoryEntityRowMapper.instance,
                        id
                )
        );
    }

    @Override
    public Optional<CategoryEntity> findUserCategoryByName(String username, String category) {
        return Optional.ofNullable(
                spendJdbcTemplate.queryForObject(
                        "SELECT * FROM \"category\" WHERE \"category\".category = ? and username = ?",
                        CategoryEntityRowMapper.instance,
                        category,
                        username
                )
        );
    }
}
