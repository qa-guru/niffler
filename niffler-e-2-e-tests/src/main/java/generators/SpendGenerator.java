package generators;

import constant.CurrencyValues;
import constant.SpendsCategories;
import model.SpendDto;

import static constant.DefaultData.*;

public class SpendGenerator {

    public static SpendDto defaultSpend() {
        SpendDto spend = new SpendDto();
        spend.setUsername(DEFAULT_USER)
                .setAmount(random(1000, 9999))
                .setCategory(SpendsCategories.Test.spendsCategories)
                .setCurrency(CurrencyValues.RUB.toString())
                .setSpendDate(DATE_CURRENT.concat("+00:00"))
                .setDescription("test".concat(random(10000, 99999).toString()));
        return spend;
    }

    private static Integer random(int min, int max) {
        max -= min;
        return ((int) (Math.random() * ++max) + min);
    }
}
