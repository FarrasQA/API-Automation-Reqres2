package tests;

import assertions.ResponseTimeAssertions;
import assertions.StatusCodeAssertions;
import assertions.UserAssertions;
import core.ApiReportManager;
import core.BaseApi;
import core.ConfigReader;
import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertEquals;

public class GetSingleUserTest extends BaseApi {

    @Test(groups = "smoke")
    public void ts1_api_successGetSingleUser() {

        int userId = Integer.parseInt(
                ConfigReader.getProperty("single.user.id")
        );

        String expectedEmailDomain =
                ConfigReader.getProperty("single.user.email.domain");

        Response response =
                given()
                        .spec(request)
                        .pathParam("id", userId)
                        .when()
                        .get(UserEndpoints.GET_SINGLE_USER);

        ApiReportManager.attach(
                "GET",
                UserEndpoints.GET_SINGLE_USER,
                null,
                response
        );

        int actualId = response.jsonPath().getInt("data.id");
        String actualEmail = response.jsonPath().getString("data.email");

        boolean emailValid = actualEmail.contains(expectedEmailDomain);

        ApiReportManager.addAssertion("data.id", String.valueOf(actualId));
        ApiReportManager.addAssertion("email", actualEmail);
        ApiReportManager.addAssertion("email domain valid",
                String.valueOf(emailValid));

        StatusCodeAssertions.validateStatusCode(response, 200);

        UserAssertions.validateSingleUser(
                response,
                userId,
                expectedEmailDomain
        );

        ResponseTimeAssertions.validateResponseTimeUnder(
                response,
                Integer.parseInt(ConfigReader.getProperty("single.user.response.time.max"))
        );

        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/single-user-schema.json"));
    }

    @Test(groups = "smoke")
    public void ts2_api_userNotFound() {

        int userId = Integer.parseInt(
                ConfigReader.getProperty("single.user.not.found.id")
        );

        Response response =
                given()
                        .spec(request)
                        .pathParam("id", userId)
                        .when()
                        .get(UserEndpoints.GET_SINGLE_USER);

        ApiReportManager.attach(
                "GET",
                UserEndpoints.GET_SINGLE_USER,
                null,
                response
        );

        int actualStatus = response.statusCode();
        String body = response.getBody().asString().trim();

        boolean isEmptyJson = body.equals("{}");

        // ASSERTIONS REPORT
        ApiReportManager.addAssertion("status code", String.valueOf(actualStatus));
        ApiReportManager.addAssertion("expected status", "404");
        ApiReportManager.addAssertion("response body empty", String.valueOf(isEmptyJson));

        StatusCodeAssertions.validateStatusCode(response, 404);

        Assert.assertEquals(body, "{}", "Expected empty JSON {}");
    }
}