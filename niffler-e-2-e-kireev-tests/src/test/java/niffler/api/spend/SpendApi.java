package niffler.api.spend;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import niffler.api.base.BaseApi;
import niffler.api.spend.dto.SpendDto;
import niffler.api.spend.endpoint.SpendEndpoint;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public final class SpendApi extends BaseApi {

    @Step("Spend api: add new spend")
    public static SpendDto add(SpendDto spendDto) {
        Allure.addAttachment("New spend", spendDto.toString());
        return given().spec(SPEND_APP_REQUEST_SPEC)
                    .body(spendDto.toJson())
                .when()
                    .post(SpendEndpoint.ADD)
                .then().spec(DEFAULT_RESPONSE_SPEC)
                    .statusCode(201)
                    .extract().as(SpendDto.class);
    }

    @Step("Spend api: delete spends")
    public static void delete(String username, UUID... ids) {
        given().spec(SPEND_APP_REQUEST_SPEC)
                    .param("username", username)
                    .param("ids", List.of(ids))
                .when()
                    .delete(SpendEndpoint.DELETE)
                .then().spec(DEFAULT_RESPONSE_SPEC)
                    .statusCode(202);
    }

    public static void delete(SpendDto... spends) {
        for (SpendDto spend : spends)
            delete(spend.getUsername(), spend.getId());
    }

    public static void delete(List<SpendDto> spends) {
        delete(spends.toArray(new SpendDto[0]));
    }

}
