package guru.qa.niffler.db.model;

import java.util.UUID;

public class UserDataEntity {

  private UUID id;
  private String username;
  private String firstname;
  private String surname;
  private CurrencyValues currency;
  private String photo;
  private FriendState friendState;

  public UserDataEntity() {
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public FriendState getFriendState() {
    return friendState;
  }

  public void setFriendState(FriendState friendState) {
    this.friendState = friendState;
  }
}