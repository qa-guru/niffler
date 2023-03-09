package niffler.data.repository;

import niffler.data.FriendsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FriendsRepository extends JpaRepository<FriendsEntity, UUID> {

    List<FriendsEntity> findAllByUserId(UUID userId);
}
