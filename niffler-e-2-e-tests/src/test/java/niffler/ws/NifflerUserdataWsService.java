package niffler.ws;

import niffler.ws.model.wsdl.AllUsersRequest;
import niffler.ws.model.wsdl.AllUsersResponse;
import niffler.ws.model.wsdl.CurrentUserRequest;
import niffler.ws.model.wsdl.CurrentUserResponse;
import niffler.ws.model.wsdl.UpdateUserInfoRequest;
import niffler.ws.model.wsdl.UpdateUserInfoResponse;
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
