package core;

import io.restassured.response.Response;

public class ApiReportManager {

    private static final ThreadLocal<ApiInfo> apiInfo =
            ThreadLocal.withInitial(ApiInfo::new);

    /**
     * Attach Request & Response Information
     */
    public static void attach(
            String method,
            String endpoint,
            Object requestBody,
            Response response
    ) {

        ApiInfo info = apiInfo.get();

        // Request
        info.setMethod(method);
        info.setEndpoint(endpoint);

        info.setRequestBody(
                requestBody != null
                        ? requestBody.toString()
                        : "No Request Body"
        );

        // Response
        if (response != null) {

            info.setStatusCode(
                    response.statusCode()
            );

            info.setResponseTime(
                    response.getTime()
            );

            info.setResponseBody(
                    response.asPrettyString()
            );
        }
    }

    /**
     * Attach Assertion Result
     */
    public static void addAssertion(
            String key,
            Object value
    ) {

        apiInfo.get()
                .getAssertions()
                .put(key, value);
    }

    /**
     * Get API Information
     */
    public static ApiInfo getApiInfo() {

        return apiInfo.get();
    }

    /**
     * Clear ThreadLocal after each test
     */
    public static void clear() {

        apiInfo.remove();
    }
}