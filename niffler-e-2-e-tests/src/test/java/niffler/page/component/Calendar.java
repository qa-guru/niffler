package niffler.page.component;

import com.codeborne.selenide.SelenideElement;

public class Calendar extends BaseComponent<Calendar>{
    public Calendar(SelenideElement self) {
        super(self);
    }

    public void setDateAsValue(String date) {
        self.$(".react-datepicker__input-container input").setValue(date);
    }
}
