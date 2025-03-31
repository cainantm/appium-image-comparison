import io.appium.java_client.AppiumBy;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URISyntaxException;
import java.time.Duration;

public class ApplitoolsVisualTest extends BaseApplitoolsTest{

    private final static String CHECK_HOME = "home_screen";
    private final static String CHECK_LOGIN = "login_screen";

    private final static By LOGIN_SCREEN = AppiumBy.accessibilityId("Login Screen");
    private final static By USERNAME_FIELD = AppiumBy.accessibilityId("username");

    @Override
    protected DesiredCapabilities getCaps() throws URISyntaxException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium:platformName", "Android");
        capabilities.setCapability("appium:deviceName", "Android Emulator");
        capabilities.setCapability("appium:automationName", "UiAutomator2");
        capabilities.setCapability("appium:platformVersion", "8.0");
        capabilities.setCapability("appium:app", "C:/Users/caina/OneDrive/Desktop/Appium/Appium-Automated-Visual-Testing/Visual-Testing/src/test/resources/apps/TheApp-v2.apk");
        capabilities.setCapability("appium:uninstallOtherPackages", "io.cloudgrey.the_app");

        return capabilities;
    }

    private WebElement waitForElement(WebDriverWait wait, By selector) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(selector));
        try { Thread.sleep(750); } catch (InterruptedException ign) {}
        return el;
    }

    @Test
    public void testAppDesign() {
        eyes.open(driver, "TheApp", "basic design test");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement loginScreen = waitForElement(wait, LOGIN_SCREEN);

        eyes.checkWindow(CHECK_HOME);

        loginScreen.click();
        waitForElement(wait, USERNAME_FIELD);

        eyes.checkWindow(CHECK_LOGIN);
        eyes.close();
    }
}
