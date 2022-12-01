package DHS;

import AxeClasses.AccessibilityException;
import AxeClasses.AxeHelper;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.CommonMethods;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DHSWebTest extends CommonMethods {
/*
    @Parameters({"host", "security token"})
    @BeforeClass
    public void beforeClass(String host, String securityToken) throws MalformedURLException, IOException {
        System.out.println("Run started");
        //COPY AND PASTE CAPABILITIES FOR VM HERE
        DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);
        capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("platformVersion", "11");
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "104");
        capabilities.setCapability("location", "US East");
        capabilities.setCapability("resolution", "1024x768");
        capabilities.setCapability("securityToken", securityToken); // TODO - Comment out if using perfecto user/password


        remoteWebDriver = new RemoteWebDriver(new URL("https://"+cloudName+".perfectomobile.com/nexperience/perfectomobile/wd/hub/fast"), capabilities);
        remoteWebDriver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
    }


*/
    @AfterClass
    public void afterClass() {
        try {
            remoteWebDriver.close();
            remoteWebDriver.getCapabilities().getCapability("reportPdfUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }

        remoteWebDriver.quit();
        System.out.println("Test complete");
    }
    @Test
    public void homepageTest() throws MalformedURLException {

        System.out.println("Run started");
        //COPY AND PASTE CAPABILITIES FOR VM HERE
        DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);
        capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("platformVersion", "11");
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "104");
        capabilities.setCapability("location", "US East");
        capabilities.setCapability("resolution", "1024x768");
        capabilities.setCapability("securityToken", securityToken); // TODO - Comment out if using perfecto user/password


        remoteWebDriver = new RemoteWebDriver(new URL("https://"+cloudName+".perfectomobile.com/nexperience/perfectomobile/wd/hub/fast"), capabilities);
        remoteWebDriver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);

        PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                .withProject(new Project("ADA_w_Axe", "1.0"))
                .withJob(new Job("Demo - Axe ADA", 45))
                .withContextTags("Accessibility")
                .withWebDriver(remoteWebDriver)
                .build();

        //set up ReportiumClient (DO NOT REMOVE)
        ReportiumClient reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);

        //Establish error count
        int errorCount = 0;

        String reportUrl = reportiumClient.getReportUrl();


        //Run the test
        try {


            reportiumClient.testStart("ADA Test with Axe on Capgemini (" +  dhsHomePage +  ")", new TestContext("ADA", "axe"));

            reportiumClient.stepStart("step 1: Load Page");
            remoteWebDriver.get(dhsHomePage);

            reportiumClient.stepStart("step 2: Capture Screenshot ");
            remoteWebDriver.getScreenshotAs(OutputType.BASE64);

            reportiumClient.stepStart("step 3: Running AXE Framework");
            AxeHelper axe = new AxeHelper(remoteWebDriver);
            axe.runAxe();

            reportiumClient.stepStart("Step 4: A11y violations");
            axe.startHighlighter("violations");

            final StringBuilder errors = new StringBuilder();

            while (true) {
                final Map<String, ?> violation = axe.nextHighlight();
                if (violation == null) {
                    break;
                }

                errorCount++;
                final String ruleId = (String) violation.get("issue");
                final Map<String, String> node = (Map<String, String>) violation.get("node");

                final String impact = node.get("impact");
                final String summary = node.get("failureSummary");
                final String html = node.get("html");
                final String selector = (String) violation.get("target");

                final String message = String.format("%s - %s%n %s%n Selector:\t%s%n HTML:\t\t%s%n%n",
                        impact, ruleId, summary, selector, html);

                remoteWebDriver.getScreenshotAs(OutputType.BASE64);
                reportiumClient.reportiumAssert(message,false);
                errors.append(message);

            }

            if (errorCount > 0) {
                final Capabilities capabilities2 = remoteWebDriver.getCapabilities();
                final String platform = String.valueOf(capabilities2.getCapability("platformName"));
                final String version = String.valueOf(capabilities2.getCapability("platformVersion"));
                final String browserName = String.valueOf(capabilities2.getCapability("browserName"));
                final String browserVersion = String.valueOf(capabilities2.getCapability("browserVersion"));

                String browserVersionFormatted;
                if ("null".equals(browserName)) {
                    browserVersionFormatted = "default browser";
                } else {
                    browserVersionFormatted = browserName + "-" + browserVersion;
                }

                String message = String.format("%n%s-%s %s : %d violations on %s%nReport Link: %s%n",
                        platform, version, browserVersionFormatted, errorCount, dhsHomePage, reportUrl);


                message = String.format("%s%n%s%n", message, errors);

                throw new AccessibilityException(message);
            }

            reportiumClient.stepStart("Test Completed");
            reportiumClient.testStop(TestResultFactory.createSuccess());
        } catch (Exception e) {
            reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
            e.printStackTrace();

        }

    }
}
