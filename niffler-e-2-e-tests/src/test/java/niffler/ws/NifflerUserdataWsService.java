package niffler.ws;

import niffler.model.soap.AllUsersRequest;
import niffler.model.soap.AllUsersResponse;
import niffler.model.soap.CurrentUserRequest;
import niffler.model.soap.CurrentUserResponse;
import niffler.model.soap.UpdateUserInfoRequest;
import niffler.model.soap.UpdateUserInfoResponse;
import niffler.ws.service.SoapService;

public class NifflerUserdataWsService extends SoapService {
    public NifflerUserdataWsService() {
        super(CFG.userdataUrl());
    }

    private final NifflerUserdataWs userdataWs = retrofit.create(NifflerUserdataWs.class);

    public CurrentUserResponse currentUser(CurrentUserRequest currentUserRequest) throws Exception {
        return userdataWs.currentUserRequest(currentUserRequest)
                .execute()
                .body();
    }

    public UpdateUserInfoResponse updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest) throws Exception {
        return userdataWs.updateUserInfoRequest(updateUserInfoRequest)
                .execute()
                .body();
    }

    public AllUsersResponse allUsersRequest(AllUsersRequest allUsersRequest) throws Exception {
        return userdataWs.allUsersRequest(allUsersRequest)
                .execute()
                .body();
    }
}
