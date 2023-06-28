package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.SpendEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface SpendRepository extends JpaRepository<SpendEntity, UUID> {

    @Nonnull
    List<SpendEntity> findAllByUsername(@Nonnull String username);

    @Nonnull
    List<SpendEntity> findAllByUsernameAndSpendDateGreaterThanEqual(@Nonnull String username,
                                                                    @Nonnull Date dateFrom);

    @Nonnull
    List<SpendEntity> findAllByUsernameAndSpendDateLessThanEqual(@Nonnull String username,
                                                                 @Nonnull Date dateTo);

    @Nonnull
    List<SpendEntity> findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(@Nonnull String username,
                                                                                             @Nonnull Date dateFrom,
                                                                                             @Nonnull Date dateTo);
}
