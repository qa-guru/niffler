package niffler.api.base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import niffler.config.AppProperties;

public abstract class BaseApi {

    private static final RequestSpecification DEFAULT_REQUEST_SPEC = new RequestSpecBuilder()
            .log(LogDetail.ALL)
            .setContentType(ContentType.JSON)
            .build();

    protected static RequestSpecification SPEND_APP_REQUEST_SPEC = DEFAULT_REQUEST_SPEC
            .baseUri(AppProperties.SPEND_APP_URI);

    protected static ResponseSpecification DEFAULT_RESPONSE_SPEC = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();

}
