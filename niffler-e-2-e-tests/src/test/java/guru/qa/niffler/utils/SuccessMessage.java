package guru.qa.niffler.utils;

public enum SuccessMessage {
    FRIEND_DELETED("Friend is deleted"),
    INVITATION_ACCEPTED("Invitation is accepted"),
    INVITATION_DECLINED("Invitation is declined"),
    SPENDING_ADDED("Spending successfully added"),
    PROFILE_UPDATED("Profile successfully updated"),
    CATEGORY_ADDED("New category added");

    public final String content;

    SuccessMessage(String content) {
        this.content = content;
    }
}
