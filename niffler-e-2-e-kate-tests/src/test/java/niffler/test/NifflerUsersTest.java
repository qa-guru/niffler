package niffler.test;

import niffler.jupiter.User;
import niffler.page.LoginPage;
import niffler.model.UserModel;
import org.junit.jupiter.api.Test;
import niffler.jupiter.UserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static niffler.jupiter.User.UserType.ADMIN;
import static niffler.jupiter.User.UserType.COMMON;

public class NifflerUsersTest {

    @Test
    void test1(@User(userType = ADMIN) UserModel user) {
        System.out.println("#### Test 1. Username: " + user.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.login("Kate", "pass");
    }

    @Test
    void test2(@User(userType = ADMIN) UserModel userFirst,
               @User(userType = COMMON) UserModel userSecond,
               @User(userType = COMMON) UserModel userThird) {
        System.out.println("#### Test 2. Username: " + userFirst.getUsername());
        System.out.println("#### Test 2. Username: " + userSecond.getUsername());
        System.out.println("#### Test 2. Username: " + userThird.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.login("Kate", "pass");
    }

    @Test
    void test3(@User UserModel user) {
        System.out.println("#### Test 3. Username: " + user.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.login("Kate", "pass");
    }

    @Test
    void test4(@User UserModel user) {
        System.out.println("#### Test 4. Username: " + user.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.login("Kate", "pass");
    }

    @Test
    void test5(@User UserModel userFirst, @User UserModel userSecond) {
        System.out.println("#### Test 5. Username: " + userFirst.getUsername());
        System.out.println("#### Test 5. Username: " + userSecond.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.login("Kate", "pass");
    }
}
