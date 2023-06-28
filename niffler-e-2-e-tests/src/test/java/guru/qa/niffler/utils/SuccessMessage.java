package guru.qa.niffler.utils;

public enum SuccessMessage {
    FRIEND_DELETED("Friend is deleted!"),
    INVITATION_ACCEPTED("Invitation is accepted!"),
    INVITATION_DECLINED("Invitation is declined!");

    public final String content;

    SuccessMessage(String content) {
        this.content = content;
    }
}
