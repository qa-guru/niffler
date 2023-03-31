package niffler.test;

import niffler.data.dao.PostgresHibernateUsersRepository;
import niffler.data.entity.UsersEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static niffler.model.Currency.*;

public class UserFriendsTest extends BaseTest {
    static PostgresHibernateUsersRepository usersRepository = new PostgresHibernateUsersRepository();

    static UsersEntity user1 = UsersEntity.builder()
            .username("SomeUser1")
            .currency(EUR.toString())
            .firstname("SomeUser1Firstname")
            .surname("SomeUser1Surname")
            .build();
    static UsersEntity user2 = UsersEntity.builder()
            .username("SomeUser2")
            .currency(KZT.toString())
            .firstname("SomeUser2Firstname")
            .surname("SomeUser2Surname")
            .build();
    static UsersEntity user3 = UsersEntity.builder()
            .username("SomeUser3")
            .currency(USD.toString())
            .firstname("SomeUser3Firstname")
            .surname("SomeUser3Surname")
            .build();

    @BeforeAll
    static void setUp() {
        usersRepository.addUser(user1);
        usersRepository.addUser(user2);
        usersRepository.addUser(user3);
    }

    @Test
    void friendsTest() {
        user1.addFriends(user2);
        user1.addFriends(user2);
        user1.addFriends(user3);
        usersRepository.updateUser(user1);

        UsersEntity user1Updated = usersRepository.getByUsername(user1.getUsername());
        Assertions.assertEquals(user1.getFriends().size(), user1Updated.getFriends().size());
    }

    @AfterAll
    static void clear() {
        usersRepository.removeUser(user1);
        usersRepository.removeUser(user2);
        usersRepository.removeUser(user3);
    }
}
