package niffler.api.user.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import niffler.api.base.BaseDto;

import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public final class UserDto extends BaseDto {

    public static UserDto fromJson(String json) {
        return GSON.fromJson(json, UserDto.class);
    }

    private UUID id;
    @SerializedName("username")
    private String userName;
    @SerializedName("firstname")
    private String firstName;
    private String surname;
    private Currency currency;
    private byte[] photo;

    public void verifyAfterUpdate(UserDto otherUser) {
        assertNotNull(id);
        assertEquals(otherUser.userName, userName);
        assertEquals(otherUser.firstName, firstName);
        assertEquals(otherUser.surname, surname);
        assertEquals(otherUser.currency, currency);
        assertEquals(otherUser.photo, photo);
    }

    public String toJson() {
        return GSON.toJson(this);
    }

}
