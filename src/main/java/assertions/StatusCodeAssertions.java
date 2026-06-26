package assertions;

import io.restassured.response.Response;
import org.testng.Assert;

public class StatusCodeAssertions {

    public static void validateStatusCode(
            Response response,
            int expectedStatusCode
    ) {

        Assert.assertEquals(
                response.statusCode(),
                expectedStatusCode
        );
    }
}