package niffler.api.spend.dto;

import lombok.*;
import niffler.api.base.BaseDto;
import niffler.models.Category;
import niffler.models.Currency;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public final class SpendDto extends BaseDto {

    public Date spendDate;
    private Category category;
    private Currency currency;
    private Double amount;
    private String description;
    private String username;

    public String getAmountInSpendingTableStyle() {
        NumberFormat nf = DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        nf.setGroupingUsed(false);
        return nf.format(amount);
    }

    public String getDateInSpendingTableStyle() {
        DateFormat dateFormat = new SimpleDateFormat("d MMM yy");
        return dateFormat.format(spendDate);
    }

    public String toJson() {
        return GSON.toJson(this);
    }

}
