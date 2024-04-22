package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.CurrencyEntity;
import guru.qa.niffler.data.CurrencyValues;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, UUID> {

    @Nonnull
    Optional<CurrencyEntity> findByCurrency(@Nonnull CurrencyValues currency);
}
