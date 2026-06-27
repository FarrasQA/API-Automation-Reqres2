package tests;

import assertions.ResponseTimeAssertions;
import assertions.StatusCodeAssertions;
import assertions.UserAssertions;
import core.ApiReportManager;
import core.BaseApi;
import core.ConfigReader;
import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetPaginationUserTest extends BaseApi {

    @Test(groups = "smoke")
    public void ts3_successGetPaginationUser() {

        int page =
                Integer.parseInt(
                        ConfigReader.getProperty("pagination.page")
                );

        int expectedPerPage =
                Integer.parseInt(
                        ConfigReader.getProperty("pagination.per.page")
                );

        int expectedDataSize =
                Integer.parseInt(
                        ConfigReader.getProperty("pagination.data.size")
                );

        int maxResponseTime =
                Integer.parseInt(
                        ConfigReader.getProperty("pagination.user.response.time.max")
                );

        Response response =
                given()
                        .spec(request)
                        .queryParam("page", page)
                        .when()
                        .get(UserEndpoints.PAGINATION_SINGLE_USER);

        ApiReportManager.attach(
                "GET",
                UserEndpoints.PAGINATION_SINGLE_USER,
                null,
                response
        );

        int actualPage = response.jsonPath().getInt("page");
        int actualPerPage = response.jsonPath().getInt("per_page");
        int actualDataSize = response.jsonPath().getList("data").size();

        UserAssertions.validatePagination(
                response,
                page,
                expectedPerPage,
                expectedDataSize
        );

        ApiReportManager.addAssertion("page", String.valueOf(actualPage));
        ApiReportManager.addAssertion("per_page", String.valueOf(actualPerPage));
        ApiReportManager.addAssertion("data.length", String.valueOf(actualDataSize));

        ApiReportManager.addAssertion(
                "validation",
                "last_name & avatar existence checked"
        );

        StatusCodeAssertions.validateStatusCode(response, 200);

        ResponseTimeAssertions.validateResponseTimeUnder(
                response,
                maxResponseTime
        );

        response.then()
                .assertThat()
                .body(
                        matchesJsonSchemaInClasspath(
                                "schemas/pagination-user-schema.json"
                        )
                );
    }
}