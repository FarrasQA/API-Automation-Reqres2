package core;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners(TestListener.class)
public class BaseApi {

    protected RequestSpecification request;

    @BeforeMethod
    public void setupApi() {

        RestAssured.baseURI =
                ConfigReader.getProperty(
                        "base.url"
                );

        request =
                new RequestSpecBuilder()

                        .setBaseUri(
                                ConfigReader.getProperty(
                                        "base.url"
                                )
                        )

                        .setContentType(
                                ContentType.JSON
                        )

                        .addHeader(
                                "x-api-key",
                                ConfigReader.getProperty(
                                        "api.key"
                                )
                        )

                        .build();
    }
}