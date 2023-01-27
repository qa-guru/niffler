package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SelenideElement
            loginButton = $("a[href*='redirect']");
    private final SelenideElement
            registerButton = $("a[href*='register']");
    private final SelenideElement
            spendingsTable = $(".spendings-table");


    @Step("Открыть главную страницу")
    public void open() {
        Selenide.open("http://127.0.0.1:3000/");
    }

    @Step("Выбрать варианта логина")
    public void selectLogin() {
        loginButton.click();
    }

    @Step("Выбрать вариант регистрации")
    public void selectRegister() {
        registerButton.click();
    }

    @Step("Таблица трат должна иметь {spending}")
    public void spendingTableShouldHave(String spending) {
        spendingsTable.shouldHave(Condition.text(spending));
    }
}
