package com.perfecto.sampleproject;
import static org.testng.Assert.assertTrue;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;


public class LocalSelenium {

	@Test
	public void localTest() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		driver.get("https://www.dhs.gov");
		String aTitle = driver.getTitle();
		System.out.println(aTitle);
		//compare the actual title with the expected title
		assertTrue(aTitle.equals("Home | Homeland Security"), "Title verified as expected");
		driver.close();
		driver.quit();
	}

}

