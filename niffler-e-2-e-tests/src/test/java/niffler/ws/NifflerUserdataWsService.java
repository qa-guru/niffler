package niffler.ws;

import niffler.ws.model.wsdl.CurrentUserRequest;
import niffler.ws.model.wsdl.CurrentUserResponse;
import niffler.ws.service.SoapService;

public class NifflerUserdataWsService extends SoapService {
    public NifflerUserdataWsService() {
        super(CFG.userdataUrl());
    }

    private final NifflerUserdataWs userdataWs = retrofit.create(NifflerUserdataWs.class);

    public CurrentUserResponse currentUser(CurrentUserRequest currenUserMessage) throws Exception {
        return userdataWs.currentUserRequest(currenUserMessage)
                .execute()
                .body();
    }
}
