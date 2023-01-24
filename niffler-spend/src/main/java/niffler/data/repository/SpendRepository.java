package niffler.data.repository;

import niffler.data.SpendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface SpendRepository extends JpaRepository<SpendEntity, UUID> {

    List<SpendEntity> findAllByUsername(String username);

    List<SpendEntity> findAllByUsernameAndSpendDateGreaterThanEqual(String username, Date dateFrom);

    List<SpendEntity> findAllByUsernameAndSpendDateLessThanEqual(String username, Date dateTo);

    List<SpendEntity> findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(String username, Date dateFrom, Date dateTo);
}
