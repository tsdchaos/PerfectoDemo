package Pages;

import org.openqa.selenium.remote.RemoteWebDriver;
import utils.constants;

public class PageInitializer {
    public static HomePage homePage;
    public static BorderSecurity borderSecurity;

    public static CyberSecurity cyberSecurity;
    public static void initializePageObjects(RemoteWebDriver remoteWebDriver) {
        homePage = new HomePage();
        borderSecurity = new BorderSecurity();
        cyberSecurity = new CyberSecurity();
    }

}
