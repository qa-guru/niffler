package guru.qa.niffler.data;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class FriendShipId implements Serializable {

    private UUID requester;
    private UUID addressee;

    public UUID getRequester() {
        return requester;
    }

    public void setRequester(UUID requester) {
        this.requester = requester;
    }

    public UUID getAddressee() {
        return addressee;
    }

    public void setAddressee(UUID addressee) {
        this.addressee = addressee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendShipId friendsId = (FriendShipId) o;
        return Objects.equals(requester, friendsId.requester) && Objects.equals(addressee, friendsId.addressee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requester, addressee);
    }
}
