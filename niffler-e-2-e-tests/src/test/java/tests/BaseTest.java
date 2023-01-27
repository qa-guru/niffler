package tests;


public abstract class BaseTest {
    protected final String USER_LOGIN = System.getProperty("login");
    protected final String USER_PASS = System.getProperty("pass");
    protected final String USER_TOKEN = System.getProperty("token");
}
