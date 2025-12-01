package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.projection.UserWithStatus;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  @Nonnull
  Optional<UserEntity> findByUsername(String username);

  @Nonnull
  @Query(
      "select distinct new guru.qa.niffler.data.projection.UserWithStatus(u.id, u.username, u.currency, u.fullname, u.photoSmall, f.status) " +
          "from UserEntity u left join FriendshipEntity f on " +
          "(u = f.addressee and f.requester.username = :username) " +
          "where u.username <> :username " +
          "and (f.status = guru.qa.niffler.data.FriendshipStatus.PENDING or f.status is null)" +
          "order by f.status asc"
  )
  List<UserWithStatus> findByUsernameNot(String username);

  @Nonnull
  @Query(
      "select distinct new guru.qa.niffler.data.projection.UserWithStatus(u.id, u.username, u.currency, u.fullname, u.photoSmall, f.status) " +
          "from UserEntity u left join FriendshipEntity f on " +
          "(u = f.addressee and f.requester.username = :username) " +
          "where u.username <> :username " +
          "and (f.status = guru.qa.niffler.data.FriendshipStatus.PENDING or f.status is null)" +
          "and (lower(u.username) like lower(concat('%', :searchQuery, '%')) or lower(u.fullname) like lower(concat('%', :searchQuery, '%')))" +
          "order by f.status asc"
  )
  List<UserWithStatus> findByUsernameNot(@Param("username") String username,
                                         @Param("searchQuery") String searchQuery);

  @Nonnull
  @Query(
      "select distinct new guru.qa.niffler.data.projection.UserWithStatus(u.id, u.username, u.currency, u.fullname, u.photoSmall, f.status) " +
          "from UserEntity u left join FriendshipEntity f on " +
          "(u = f.addressee and f.requester.username = :username) " +
          "where u.username <> :username " +
          "and (f.status = guru.qa.niffler.data.FriendshipStatus.PENDING or f.status is null)" +
          "order by f.status asc"
  )
  Page<UserWithStatus> findByUsernameNot(String username,
                                         Pageable pageable);

  @Nonnull
  @Query(
      "select distinct new guru.qa.niffler.data.projection.UserWithStatus(u.id, u.username, u.currency, u.fullname, u.photoSmall, f.status) " +
          "from UserEntity u left join FriendshipEntity f on " +
          "(u = f.addressee and f.requester.username = :username) " +
          "where u.username <> :username " +
          "and (f.status = guru.qa.niffler.data.FriendshipStatus.PENDING or f.status is null)" +
          "and (lower(u.username) like lower(concat('%', :searchQuery, '%')) or lower(u.fullname) like lower(concat('%', :searchQuery, '%')))" +
          "order by f.status asc"
  )
  Page<UserWithStatus> findByUsernameNot(@Param("username") String username,
                                         @Param("searchQuery") String searchQuery,
                                         Pageable pageable);

  @Nonnull
  @Query(
      "select distinct new guru.qa.niffler.data.projection.UserWithStatus(u.id, u.username, u.currency, u.fullname, u.photoSmall, f.status) " +
          "from UserEntity u join FriendshipEntity f on u = f.requester where " +
          "(f.status is not null) " +
          "and f.addressee = :addressee " +
          "order by f.status desc"
  )
  List<UserWithStatus> findFriends(@Param("addressee") UserEntity addressee);

  @Nonnull
  @Query(
      "select distinct new guru.qa.niffler.data.projection.UserWithStatus(u.id, u.username, u.currency, u.fullname, u.photoSmall, f.status) " +
          "from UserEntity u join FriendshipEntity f on u = f.requester where " +
          "(f.status is not null) " +
          "and f.addressee = :addressee " +
          "and (lower(u.username) like lower(concat('%', :searchQuery, '%')) or lower(u.fullname) like lower(concat('%', :searchQuery, '%')))" +
          "order by f.status desc"
  )
  List<UserWithStatus> findFriends(@Param("addressee") UserEntity addressee,
                                   @Param("searchQuery") String searchQuery);

  @Nonnull
  @Query(
      "select distinct new guru.qa.niffler.data.projection.UserWithStatus(u.id, u.username, u.currency, u.fullname, u.photoSmall, f.status) " +
          "from UserEntity u join FriendshipEntity f on u = f.requester where " +
          "(f.status is not null) " +
          "and f.addressee = :addressee " +
          "order by f.status desc"
  )
  Page<UserWithStatus> findFriends(@Param("addressee") UserEntity addressee,
                                   Pageable pageable);

  @Nonnull
  @Query(
      "select distinct new guru.qa.niffler.data.projection.UserWithStatus(u.id, u.username, u.currency, u.fullname, u.photoSmall, f.status) " +
          "from UserEntity u join FriendshipEntity f on u = f.requester where " +
          "(f.status is not null) " +
          "and f.addressee = :addressee " +
          "and (lower(u.username) like lower(concat('%', :searchQuery, '%')) or lower(u.fullname) like lower(concat('%', :searchQuery, '%')))" +
          "order by f.status desc"
  )
  Page<UserWithStatus> findFriends(@Param("addressee") UserEntity addressee,
                                   @Param("searchQuery") String searchQuery,
                                   Pageable pageable);
}
