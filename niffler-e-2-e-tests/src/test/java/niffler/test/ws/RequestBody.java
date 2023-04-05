package niffler.test.ws;

import niffler.ws.model.wsdl.CurrentUserRequest;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "soapenv:Body", strict = false)
public class RequestBody {
    @Element(name = "gs:currentUserRequest", required = true)
    private CurrentUserRequest requestData;

    public CurrentUserRequest getRequestData() {
        return requestData;
    }

    public void setRequestData(CurrentUserRequest requestData) {
        this.requestData = requestData;
    }
}
