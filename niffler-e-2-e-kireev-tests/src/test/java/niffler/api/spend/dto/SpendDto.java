package niffler.api.spend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import niffler.api.base.BaseDto;
import niffler.models.Category;
import niffler.models.Currency;

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

    public String toJson() {
        return GSON.toJson(this);
    }

}
