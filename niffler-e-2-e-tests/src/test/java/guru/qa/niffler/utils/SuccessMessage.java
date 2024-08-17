package guru.qa.niffler.utils;

public enum SuccessMessage {
  FRIEND_DELETED("Friend is deleted"),
  INVITATION_ACCEPTED("Invitation is accepted"),
  INVITATION_DECLINED("Invitation is declined"),
  SPENDING_ADDED("New spending is successfully created"),
  PROFILE_UPDATED("Profile successfully updated"),
  CATEGORY_ADDED("You've added new category:");

  public final String content;

  SuccessMessage(String content) {
    this.content = content;
  }
}
