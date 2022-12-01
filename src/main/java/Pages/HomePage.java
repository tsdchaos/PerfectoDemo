package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import static utils.PerfectoElements.remoteWebDriver;

public class HomePage {

    public WebElement schoolSafeBtn = remoteWebDriver.findElement(By.xpath("//*[@id='block-mainpagecontent']/article/div/div[3]/div[1]/div/div/div/div/div/ul/li[1]/div/div[3]/a/span"));

    public WebElement newsUpdates = remoteWebDriver.findElement(By.xpath("//*[@id='block-mainpagecontent']/article/div/div[3]/div[2]/div/div/div/div/div/div/div/div/div/div/div/div[1]/div/div/div/div[2]/div/div[1]/h2"));

    public String titleHome = "Home | Homeland Security";

}
