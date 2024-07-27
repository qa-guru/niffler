package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.UserEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Nonnull
    Optional<UserEntity> findByUsername(@Nonnull String username);

    @Nonnull
    List<UserEntity> findByUsernameNot(@Nonnull String username);

    @Nonnull
    @Query("select u from UserEntity u where u.username <> :username" +
            " and (u.username like %:searchQuery% or u.fullname like %:searchQuery%)")
    List<UserEntity> findByUsernameNot(@Nonnull @Param("username") String username,
                                       @Nonnull @Param("searchQuery") String searchQuery);

    @Nonnull
    Page<UserEntity> findByUsernameNot(@Nonnull String username,
                                       @Nonnull Pageable pageable);

    @Nonnull
    @Query("select u from UserEntity u where u.username <> :username" +
            " and (u.username like %:searchQuery% or u.fullname like %:searchQuery%)")
    Page<UserEntity> findByUsernameNot(@Nonnull @Param("username") String username,
                                       @Nonnull @Param("searchQuery") String searchQuery,
                                       @Nonnull Pageable pageable);

    @Nonnull
    @Query(
            "select u from UserEntity u join FriendshipEntity f on u = f.requester where " +
                    "(f.status = guru.qa.niffler.data.FriendshipStatus.ACCEPTED or f.status = guru.qa.niffler.data.FriendshipStatus.PENDING) " +
                    "and f.addressee = :addressee"
    )
    List<UserEntity> findFriends(@Param("addressee") UserEntity addressee);

    @Nonnull
    @Query(
            "select u from UserEntity u join FriendshipEntity f on u = f.requester where " +
                    "(f.status = guru.qa.niffler.data.FriendshipStatus.ACCEPTED or f.status = guru.qa.niffler.data.FriendshipStatus.PENDING) " +
                    "and f.addressee = :addressee " +
                    "and (u.username like %:searchQuery% or u.fullname like %:searchQuery%)"
    )
    List<UserEntity> findFriends(@Param("addressee") UserEntity addressee,
                                 @Param("searchQuery") String searchQuery);

    @Nonnull
    @Query(
            "select u from UserEntity u join FriendshipEntity f on u = f.requester where " +
                    "(f.status = guru.qa.niffler.data.FriendshipStatus.ACCEPTED or f.status = guru.qa.niffler.data.FriendshipStatus.PENDING) " +
                    "and f.addressee = :addressee"
    )
    Page<UserEntity> findFriends(@Param("addressee") UserEntity addressee,
                                 @Nonnull Pageable pageable);

    @Nonnull
    @Query(
            "select u from UserEntity u join FriendshipEntity f on u = f.requester where " +
                    "(f.status = guru.qa.niffler.data.FriendshipStatus.ACCEPTED or f.status = guru.qa.niffler.data.FriendshipStatus.PENDING) " +
                    "and f.addressee = :addressee " +
                    "and (u.username like %:searchQuery% or u.fullname like %:searchQuery%)"
    )
    Page<UserEntity> findFriends(@Param("addressee") UserEntity addressee,
                                 @Param("searchQuery") String searchQuery,
                                 @Nonnull Pageable pageable);
}
