package niffler.ws;

import niffler.test.ws.RequestEnvelope;
import niffler.ws.service.SoapService;

public class NifflerUserdataWsService extends SoapService {
    public NifflerUserdataWsService() {
        super(CFG.userdataUrl());
    }

    private final NifflerUserdataWs userdataWs = retrofit.create(NifflerUserdataWs.class);

    public String currentUser(RequestEnvelope userJson) throws Exception {
        return userdataWs.currentUserRequest(userJson)
                .execute()
                .body();
    }
}
