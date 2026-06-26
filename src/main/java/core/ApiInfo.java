package core;

import java.util.LinkedHashMap;
import java.util.Map;

public class ApiInfo {

    // Request
    private String method;
    private String endpoint;
    private String requestBody;

    // Response
    private int statusCode;
    private long responseTime;
    private String responseBody;

    // Assertion
    private final Map<String, Object> assertions =
            new LinkedHashMap<>();

    // Request
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    // Response
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    private Integer expectedStatusCode;

    public Integer getExpectedStatusCode() {
        return expectedStatusCode;
    }

    public void setExpectedStatusCode(Integer expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    // Assertions
    public Map<String, Object> getAssertions() {
        return assertions;
    }

}