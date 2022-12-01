package utils;

import Pages.PageInitializer;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.sampleproject.PerfectoLabUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.exception.ReportiumException;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static Pages.PageInitializer.homePage;
import static Pages.PageInitializer.initializePageObjects;
import static org.testng.Assert.assertTrue;
import static utils.constants.driver;

public class CommonMethods extends PerfectoElements {

    public static final int EXPLICIT_WAIT = 15;

    public static void getTitle(){
        String aTitle = driver.getTitle();
        assertTrue(aTitle.equals(homePage.titleHome), "Title verified as expected");

    }
    public static void checkAccess(JavascriptExecutor js, String stepName){
        //declare the Map for script parameters
        Map<String, Object> params = new HashMap<>();
        params.put("tag", stepName);
        js.executeScript("mobile:checkAccessibility:audit", params);
    }

    public static void closeBrowser() {
        driver.quit();
    }




    public static void establishRemoteConnectionAndroid() throws Exception{
        //establish instance of PerfectElements
        String browserName = "mobileOS";
        PerfectoElements perfectoElements = new PerfectoElements();
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "10");
        capabilities.setCapability("location", "EU-DE-FRA");
        capabilities.setCapability("resolution", "1440x2960");
        capabilities.setCapability("manufacturer", "Samsung");
        capabilities.setCapability("model", "Galaxy S9");

        // The below capability is mandatory. Please do not replace it.
        capabilities.setCapability("securityToken", PerfectoLabUtils.fetchSecurityToken(perfectoElements.securityToken));


        perfectoElements.remoteWebDriver= new RemoteWebDriver(new URL("https://" + PerfectoLabUtils.fetchCloudName(perfectoElements.cloudName) + ".perfectomobile.com/nexperience/perfectomobile/wd/hub"), capabilities);
        perfectoElements.remoteWebDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        perfectoElements.remoteWebDriver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);

        perfectoElements.reportiumClient = PerfectoLabUtils.setReportiumClient(perfectoElements.remoteWebDriver, perfectoElements.reportiumClient); //Creates reportiumClient
        perfectoElements.reportiumClient.testStart("Perfecto desktop web test", new TestContext("tag2", "tag3")); //Starts the reportium test
        perfectoElements.reportiumClient.stepStart("browser navigate to dhs"); //Starts a reportium step
        perfectoElements.remoteWebDriver.get("https://www.dhs.gov");
        perfectoElements.reportiumClient.stepEnd();

        perfectoElements.reportiumClient.stepStart("Check access");
        JavascriptExecutor js = (JavascriptExecutor) perfectoElements.remoteWebDriver;
        //declare the Map for script parameters
        Map<String, Object> params = new HashMap<>();
        params.put("tag", "login-screen");
        js.executeScript("mobile:checkAccessibility:audit", params);
        perfectoElements.reportiumClient.stepEnd();
    }

    public static WebDriverWait getWait(){
        WebDriverWait wait = new WebDriverWait(remoteWebDriver, EXPLICIT_WAIT);
        return wait;
    }
    public static void waitForClickability(WebElement element){
        getWait().until(ExpectedConditions.elementToBeClickable(element));
    }


}
