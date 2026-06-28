plugins {
    id("java")
}

group = "farras.api.automation"
version = "1.0-Reqres"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.rest-assured:rest-assured:6.0.0")
    implementation("org.testng:testng:7.12.0")
    testImplementation("org.assertj:assertj-core:3.27.7")
    implementation("com.aventstack:extentreports:5.1.2")
    implementation("io.rest-assured:json-path:6.0.0")
    implementation("org.apache.logging.log4j:log4j-core:2.26.0")
    implementation("org.slf4j:slf4j-api:2.0.18")
    implementation("io.rest-assured:json-schema-validator:6.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.0")
    implementation("io.github.cdimascio:dotenv-java:3.2.0")
    implementation("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")

}

tasks.test {
    useTestNG()

    val suite = System.getProperty("suite")

    if (suite != null) {

        useTestNG {
            suites(suite)
        }
    }

    testLogging {

        events(
            "passed",
            "skipped",
            "failed"
        )

        exceptionFormat =
            org.gradle.api.tasks.testing.logging
                .TestExceptionFormat.FULL
    }
}