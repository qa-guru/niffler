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

    @Query("select u from UserEntity u where u.username <> :username" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.surname like %:searchQuery%)")
    List<UserEntity> findByUsernameNot(@Nonnull @Param("username") String username,
                                       @Nonnull @Param("searchQuery") String searchQuery);

    @Nonnull
    Page<UserEntity> findByUsernameNot(@Nonnull String username,
                                       @Nonnull Pageable pageable);

    @Query("select u from UserEntity u where u.username <> :username" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.surname like %:searchQuery%)")
    Page<UserEntity> findByUsernameNot(@Nonnull @Param("username") String username,
                                       @Nonnull @Param("searchQuery") String searchQuery,
                                       @Nonnull Pageable pageable);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
            " where f.status = guru.qa.niffler.data.FriendshipStatus.ACCEPTED and f.requester = :requester")
    List<UserEntity> findFriends(@Param("requester") UserEntity requester);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
            " where f.status = guru.qa.niffler.data.FriendshipStatus.ACCEPTED and f.requester = :requester" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.surname like %:searchQuery%)")
    List<UserEntity> findFriends(@Param("requester") UserEntity requester,
                                 @Param("searchQuery") String searchQuery);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
            " where f.status = guru.qa.niffler.data.FriendshipStatus.ACCEPTED and f.requester = :requester")
    Page<UserEntity> findFriends(@Param("requester") UserEntity requester,
                                 @Nonnull Pageable pageable);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
            " where f.status = guru.qa.niffler.data.FriendshipStatus.ACCEPTED and f.requester = :requester" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.surname like %:searchQuery%)")
    Page<UserEntity> findFriends(@Param("requester") UserEntity requester,
                                 @Param("searchQuery") String searchQuery,
                                 @Nonnull Pageable pageable);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
            " where f.status = guru.qa.niffler.data.FriendshipStatus.PENDING and f.requester = :requester")
    List<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
            " where f.status = guru.qa.niffler.data.FriendshipStatus.PENDING and f.requester = :requester" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.surname like %:searchQuery%)")
    List<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester,
                                            @Param("searchQuery") String searchQuery);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
            " where f.status = guru.qa.niffler.data.FriendshipStatus.PENDING and f.requester = :requester")
    Page<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester,
                                            @Nonnull Pageable pageable);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
            " where f.status = guru.qa.niffler.data.FriendshipStatus.PENDING and f.requester = :requester" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.surname like %:searchQuery%)")
    Page<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester,
                                            @Param("searchQuery") String searchQuery,
                                            @Nonnull Pageable pageable);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.requester" +
            " where f.status = guru.qa.niffler.data.FriendshipStatus.PENDING and f.addressee = :addressee")
    List<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.requester" +
            " where f.status = guru.qa.niffler.data.FriendshipStatus.PENDING and f.addressee = :addressee" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.surname like %:searchQuery%)")
    List<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee,
                                           @Param("searchQuery") String searchQuery);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.requester" +
            " where f.status = guru.qa.niffler.data.FriendshipStatus.PENDING and f.addressee = :addressee")
    Page<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee,
                                           @Nonnull Pageable pageable);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.requester" +
            " where f.status = guru.qa.niffler.data.FriendshipStatus.PENDING and f.addressee = :addressee" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.surname like %:searchQuery%)")
    Page<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee,
                                           @Param("searchQuery") String searchQuery,
                                           @Nonnull Pageable pageable);
}
