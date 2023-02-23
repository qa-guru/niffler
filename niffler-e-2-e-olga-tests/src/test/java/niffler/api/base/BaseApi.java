package niffler.api.base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public abstract class BaseApi {

    protected static final RequestSpecification DEFAULT_REQUEST_SPEC = new RequestSpecBuilder()
            .log(LogDetail.ALL)
            .setContentType(ContentType.JSON)
            .build();

    protected final static ResponseSpecification DEFAULT_RESPONSE_SPEC = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();

}
