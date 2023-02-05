package niffler.api.base.spend;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import niffler.api.base.Spec;
import niffler.api.base.spend.dto.SpendDto;
import niffler.api.base.spend.endpoint.ApiUrl;

import static io.restassured.RestAssured.given;

public final class SpendApi extends Spec {

    @Step("Spend api: add new spend")
    public static SpendDto add(SpendDto spendDto) {
        Allure.addAttachment("New spend", spendDto.toString());
        return given().spec(SPEND_APP_REQUEST_SPEC)
                .body(spendDto.toJson())
                .when()
                .post(ApiUrl.ADD_SPEND)
                .then().spec(DEFAULT_RESPONSE_SPEC)
                .statusCode(201)
                .extract().as(SpendDto.class);
    }

}
