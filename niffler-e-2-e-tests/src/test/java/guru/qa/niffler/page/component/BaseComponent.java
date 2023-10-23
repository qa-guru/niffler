package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

public abstract class BaseComponent<T extends BaseComponent> {

    protected final SelenideElement self;

    public BaseComponent(@Nonnull SelenideElement self) {
        this.self = self;
    }

    @Nonnull
    public SelenideElement getSelf() {
        return self;
    }
}
