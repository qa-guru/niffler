package niffler.data.repository;

import niffler.data.FriendsEntity;
import niffler.data.FriendsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<FriendsEntity, FriendsId> {


}
