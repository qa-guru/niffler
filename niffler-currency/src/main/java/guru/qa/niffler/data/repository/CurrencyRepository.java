package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.CurrencyEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, UUID> {

    @Nullable
    CurrencyEntity findByCurrency(@Nonnull String currency);
}
