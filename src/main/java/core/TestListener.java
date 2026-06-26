package core;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {



    private static final Logger log =
            LogManager.getLogger(TestListener.class);

    private static ExtentReports extent;

    private static final ThreadLocal<ExtentTest> extentTest =
            new ThreadLocal<>();

    private static final String REPORT_DIR =
            System.getProperty("user.dir") + "/reports/";

    @Override
    public void onStart(ITestContext context) {

        System.out.println("========== TEST LISTENER STARTED ==========");

        log.info(
                "========== Test Suite Started : {} ==========",
                context.getName()
        );

        new File(REPORT_DIR).mkdirs();

        String timestamp =
                new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
                        .format(new Date());

        String reportPath =
                REPORT_DIR
                        + "extent-report_"
                        + timestamp
                        + ".html";

        ExtentSparkReporter sparkReporter =
                new ExtentSparkReporter(reportPath);

        sparkReporter.config().setTheme(
                Theme.STANDARD
        );

        sparkReporter.config().setDocumentTitle(
                "API Automation Report"
        );

        sparkReporter.config().setReportName(
                "REST Assured Test Report"
        );

        sparkReporter.config().setEncoding(
                "UTF-8"
        );

        sparkReporter.config().setTimeStampFormat(
                "EEEE, MMMM dd, yyyy, HH:mm:ss"
        );

        extent = new ExtentReports();

        extent.attachReporter(
                sparkReporter
        );

        extent.setSystemInfo(
                "OS",
                System.getProperty("os.name")
        );

        extent.setSystemInfo(
                "Java Version",
                System.getProperty("java.version")
        );

        extent.setSystemInfo(
                "User",
                System.getProperty("user.name")
        );

        extent.setSystemInfo(
                "Environment",
                System.getProperty(
                        "env",
                        "staging"
                )
        );

        extent.setSystemInfo(
                "Base URL",
                System.getProperty(
                        "base_url",
                        "-"
                )
        );

        log.info(
                "Extent Report initialized : {}",
                reportPath
        );
    }

    @Override
    public void onTestStart(ITestResult result) {

        ExtentTest test =
                extent.createTest(
                        result.getMethod().getMethodName(),
                        result.getMethod().getDescription()
                );

        test.assignCategory(
                result.getMethod().getGroups()
        );

        extentTest.set(test);

        test.info("Test execution started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        extentTest.get().pass("Test Passed");

        attachApiInformation();

        attachExecutionTime(result);

        ApiReportManager.clear();
    }

    @Override
    public void onTestFailure(ITestResult result) {

        Throwable throwable =
                result.getThrowable();

        extentTest.get().fail("Test Failed");

        if (throwable != null) {

            extentTest.get().fail(throwable);
        }

        attachApiInformation();

        attachExecutionTime(result);

        ApiReportManager.clear();
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        extentTest.get().skip("Test Skipped");

        ApiReportManager.clear();
    }

    @Override
    public void onFinish(ITestContext context) {

        if (extent != null) {
            extent.flush();
        }

        log.info(
                "Test Suite Finished : {}",
                context.getName()
        );
    }

    // API INFORMATION

    private void attachApiInformation() {

        ApiInfo apiInfo = ApiReportManager.getApiInfo();

        if (apiInfo == null
                || apiInfo.getMethod() == null
                || apiInfo.getMethod().isBlank()) {

            extentTest.get().warning(
                    "No API information attached."
            );

            return;
        }

        // =====================================================
        // REQUEST
        // =====================================================

        addInfo(
                "Method",
                apiInfo.getMethod()
        );

        addInfo(
                "Endpoint",
                apiInfo.getEndpoint()
        );

        addJson(
                "Request Body",
                apiInfo.getRequestBody()
        );

        // =====================================================
        // RESPONSE
        // =====================================================

        addInfo(
                "Status Code",
                apiInfo.getStatusCode()
        );

        if (apiInfo.getExpectedStatusCode() != null) {

            if (apiInfo.getStatusCode() == apiInfo.getExpectedStatusCode()) {
                extentTest.get().pass("HTTP Status : MATCH EXPECTATION");
            } else {
                extentTest.get().fail("HTTP Status : NOT MATCH EXPECTATION");
            }

        } else {

            if (apiInfo.getStatusCode() >= 200 && apiInfo.getStatusCode() < 300) {
                extentTest.get().pass("HTTP Status : SUCCESS");
            } else {
                extentTest.get().fail("HTTP Status : FAILED");
            }
        }

        addInfo(
                "Response Time",
                apiInfo.getResponseTime() + " ms"
        );

        addJson(
                "Response Body",
                apiInfo.getResponseBody()
        );

        // =====================================================
        // ASSERTIONS
        // =====================================================

        if (apiInfo.getAssertions() == null
                || apiInfo.getAssertions().isEmpty()) {

            extentTest.get().warning(
                    "No assertion information attached."
            );

        } else {

            extentTest.get().info("--------------------------------");
            extentTest.get().info("ASSERTIONS");
            extentTest.get().info("--------------------------------");

            apiInfo.getAssertions().forEach((key, value) -> {

                extentTest.get().log(
                        Status.PASS,
                        key + " : " + value
                );

            });
        }
    }

    // EXECUTION TIME

    private void attachExecutionTime(
            ITestResult result
    ) {

        long duration =
                result.getEndMillis()
                        - result.getStartMillis();

        extentTest.get().info(
                String.format(
                        "Execution Time : %d ms",
                        duration
                )
        );
    }

    // REPORT HELPERS

    private void addInfo(
            String title,
            Object value
    ) {

        if (value == null) {
            return;
        }

        extentTest.get().log(
                Status.INFO,
                "<b>"
                        + title
                        + "</b> : "
                        + value
        );
    }

    private void addJson(
            String title,
            Object value
    ) {

        if (value == null) {
            return;
        }

        String json = value.toString();

        if (json.isBlank()) {
            return;
        }

        extentTest.get().log(
                Status.INFO,
                "<b>"
                        + title
                        + "</b><pre>"
                        + json
                        + "</pre>"
        );
    }
}