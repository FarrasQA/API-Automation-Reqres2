package assertions;

import io.restassured.response.Response;
import org.testng.Assert;

public class ResponseTimeAssertions {

    public static void validateResponseTimeUnder(
            Response response,
            long maxMilliseconds
    ) {

        Assert.assertTrue(
                response.time() < maxMilliseconds,
                "Response time exceeded limit"
        );
    }
}