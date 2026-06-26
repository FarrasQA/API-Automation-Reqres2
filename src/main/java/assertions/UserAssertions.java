package assertions;

import core.ApiReportManager;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;

public class UserAssertions {

    public static void validateSingleUser(
            Response response,
            int expectedId,
            String expectedEmailDomain
    ) {

        int actualId =
                response.jsonPath().getInt("data.id");

        String actualEmail =
                response.jsonPath().getString("data.email");

        // =========================
        // REAL TEST ASSERTIONS
        // =========================

        Assert.assertEquals(
                actualId,
                expectedId,
                "User ID mismatch."
        );

        Assert.assertTrue(
                actualEmail.contains(expectedEmailDomain),
                "Email does not contain expected domain."
        );

        // =========================
        // EXTENT REPORT ASSERTIONS
        // =========================

        ApiReportManager.addAssertion(
                "data.id",
                String.valueOf(actualId)
        );

        ApiReportManager.addAssertion(
                "expected.id",
                String.valueOf(expectedId)
        );

        ApiReportManager.addAssertion(
                "data.email",
                actualEmail
        );

        ApiReportManager.addAssertion(
                "email contains domain",
                String.valueOf(actualEmail.contains(expectedEmailDomain))
        );
    }

    public static void validatePagination(
            Response response,
            int expectedPage,
            int expectedPerPage,
            int expectedDataSize
    ) {

        int actualPage =
                response.jsonPath().getInt("page");

        int actualPerPage =
                response.jsonPath().getInt("per_page");

        List<?> users =
                response.jsonPath().getList("data");

        int actualDataSize = users.size();

        // =========================
        // REAL TEST ASSERTIONS
        // =========================

        Assert.assertEquals(
                actualPage,
                expectedPage,
                "Page mismatch."
        );

        Assert.assertEquals(
                actualPerPage,
                expectedPerPage,
                "Per page mismatch."
        );

        Assert.assertEquals(
                actualDataSize,
                expectedDataSize,
                "Unexpected number of users."
        );

        List<String> lastNames =
                response.jsonPath().getList("data.last_name");

        List<String> avatars =
                response.jsonPath().getList("data.avatar");

        boolean hasValidUser = false;

        for (int i = 0; i < actualDataSize; i++) {

            String lastName = lastNames.get(i);
            String avatar = avatars.get(i);

            if (lastName != null && !lastName.isBlank()
                    && avatar != null && !avatar.isBlank()) {

                hasValidUser = true;
                break;
            }
        }

        Assert.assertTrue(
                hasValidUser,
                "No user has both last_name and avatar."
        );

        // =========================
        // EXTENT REPORT ASSERTIONS
        // =========================

        ApiReportManager.addAssertion(
                "page",
                String.valueOf(actualPage)
        );

        ApiReportManager.addAssertion(
                "per_page",
                String.valueOf(actualPerPage)
        );

        ApiReportManager.addAssertion(
                "data.length",
                String.valueOf(actualDataSize)
        );

        ApiReportManager.addAssertion(
                "has valid user (last_name + avatar)",
                String.valueOf(hasValidUser)
        );
    }
}