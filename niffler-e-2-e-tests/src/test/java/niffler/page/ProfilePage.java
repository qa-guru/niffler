package niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.devtools.v85.profiler.model.Profile;

import static com.codeborne.selenide.Selenide.$;

public class ProfilePage extends BasePage<ProfilePage> {

    SelenideElement profileConatainer = $(".main-content__section-avatar");

    public ProfilePage fillProfile() {
        // fill
        return this;
    }

    public ProfilePage checkPageLoaded() {
        profileConatainer.shouldBe(Condition.visible);
        return this;
    }
    public ProfilePage setName(String name) {
        profileConatainer.$("input[name='firstname']").setValue(name);
        return this;
    }
    public ProfilePage checkName(String name) {
        profileConatainer.$("input[name='firstname']").shouldHave(Condition.value(name));
        return this;
    }
    public ProfilePage submitProfile() {
        profileConatainer.$("button[type='submit']").click();
        return this;
    }
}
