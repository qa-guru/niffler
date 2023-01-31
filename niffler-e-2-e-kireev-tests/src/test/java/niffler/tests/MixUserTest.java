package niffler.tests;

import niffler.jupiter.user.WithUser;
import niffler.models.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static com.codeborne.selenide.Selenide.sleep;
import static niffler.models.user.UserRole.*;
import static niffler.models.user.UserRole.ADMIN;

class MixUserTest {

    private final Random random = new Random();

    @AfterEach
    void waitSomeTime() {
        // Имитация "бурной" деятельности теста
        sleep(random.nextInt(1_000, 5_000));
    }

    @Test
    void test1(@WithUser(COMMON) User user1,
               @WithUser(COMMON) User user2) {
        System.out.println("test1");
        System.out.println(user1);
        System.out.println(user2);
    }

    @Test
    void test2(@WithUser(COMMON) User user1,
               @WithUser(MANAGER) User user2) {
        System.out.println("test2");
        System.out.println(user1);
        System.out.println(user2);
    }

    @Test
    void test3(@WithUser(ADMIN) User user1) {
        System.out.println("test3");
        System.out.println(user1);
    }

    @Test
    void test4(@WithUser(ADMIN) User user1,
               @WithUser(MANAGER) User user2,
               @WithUser(COMMON) User user3) {
        System.out.println("test4");
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);
    }

    @Test
    void test5(@WithUser(MANAGER) User user1,
               @WithUser(MANAGER) User user2,
               @WithUser(COMMON) User user3,
               @WithUser(ADMIN) User user4) {
        System.out.println("test5");
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);
        System.out.println(user4);
    }

}
