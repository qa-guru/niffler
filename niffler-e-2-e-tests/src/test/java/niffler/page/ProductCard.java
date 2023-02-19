package niffler.page;

import com.codeborne.selenide.SelenideElement;

import java.util.Map;

public class ProductCard {

    private final SelenideElement self;

   private final Map<String, SelenideElement> buttons;

    public ProductCard(SelenideElement self) {
        this.self = self;
        this.buttons = Map.of(
                "Ok", self.$("button[id='ok']"),
                "Сancel", self.$("button[id='ok']"),
                "Amth", self.$("button[id='ok']")
        );
    }

    public SelenideElement getButton(String btn) {
        return buttons.get(btn);
    }


}
