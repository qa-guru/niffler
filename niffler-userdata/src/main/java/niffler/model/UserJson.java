package niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import niffler.data.CurrencyValues;
import niffler.data.UserEntity;

import java.util.Arrays;
import java.util.Objects;

public class UserJson {

    @JsonProperty("username")
    private String userName;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("photo")
    private byte[] photo;

    public UserJson() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public CurrencyValues getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyValues currency) {
        this.currency = currency;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public static UserJson fromEntity(UserEntity entity) {
        UserJson usr = new UserJson();
        usr.setUserName(entity.getUsername());
        usr.setFirstname(entity.getFirstname());
        usr.setSurname(entity.getSurname());
        usr.setCurrency(entity.getCurrency());
        usr.setPhoto(entity.getPhoto());
        return usr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJson user = (UserJson) o;
        return Objects.equals(userName, user.userName) && Objects.equals(firstname, user.firstname) && Objects.equals(surname, user.surname) && currency == user.currency && Arrays.equals(photo, user.photo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(userName, firstname, surname, currency);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }
}
