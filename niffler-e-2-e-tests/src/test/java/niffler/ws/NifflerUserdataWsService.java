package niffler.ws;

import io.qameta.allure.Step;
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

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: currentUserRequest")
    public CurrentUserResponse currentUser(CurrentUserRequest currentUserRequest) throws Exception {
        return userdataWs.currentUserRequest(currentUserRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: updateUserInfoRequest")
    public UpdateUserInfoResponse updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest) throws Exception {
        return userdataWs.updateUserInfoRequest(updateUserInfoRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: allUsersRequest")
    public AllUsersResponse allUsersRequest(AllUsersRequest allUsersRequest) throws Exception {
        return userdataWs.allUsersRequest(allUsersRequest)
                .execute()
                .body();
    }
}
