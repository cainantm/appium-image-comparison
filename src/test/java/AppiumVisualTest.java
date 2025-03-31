import io.appium.java_client.AppiumBy;
import io.appium.java_client.imagecomparison.SimilarityMatchingOptions;
import io.appium.java_client.imagecomparison.SimilarityMatchingResult;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;
import java.time.Duration;


// SEM OPENCV4 NAO DA PRA FAZER O getImagesSimilarity
public class AppiumVisualTest extends BaseTest{

    private final static String VALIDATION_PATH = "C:\\Users\\caina\\OneDrive\\Desktop\\Appium\\Appium-Automated-Visual-Testing\\Visual-Testing\\src\\test\\resources\\validations";
    private final static String CHECK_HOME = "home_screen";
    private final static String CHECK_LOGIN = "login_screen";
    private final static String BASELINE = "BASELINE_";
    private final static double MATCH_THRESHOLD = 0.99;

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
        capabilities.setCapability("appium:uninstallOtherPackages", "io.cloudgrey.the_app"); // serve para instalar o app antes de iniciar outro

        return capabilities;
    }

    private WebElement waitForElement(WebDriverWait wait, By selector) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(selector));
        // tem sleep para garantir que as animações ja terminaram antes de tirar print
        try { Thread.sleep(750); } catch (InterruptedException ign) {}
        return el;
    }

    @Test
    public void testAppDesign() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement loginScreen = waitForElement(wait, LOGIN_SCREEN);

        doVisualCheck(CHECK_HOME);

        loginScreen.click();
        waitForElement(wait, USERNAME_FIELD);

        doVisualCheck(CHECK_LOGIN);
    }

    private void doVisualCheck(String checkName) throws Exception {
        String baselineFilename = VALIDATION_PATH + "/" + BASELINE + checkName + ".png";
        File baselineImg = new File(baselineFilename);

        if (!baselineImg.exists()) {
            System.out.printf("No baseline found for '%s' check; capturing baseline instead of checking%n", checkName);
            File newBaseline = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(newBaseline, new File(baselineFilename));
            return;
        }

        SimilarityMatchingOptions opts = new SimilarityMatchingOptions();
        opts.withEnabledVisualization();
        SimilarityMatchingResult res = driver.getImagesSimilarity(baselineImg, driver.getScreenshotAs(OutputType.FILE), opts);

        if (res.getScore() < MATCH_THRESHOLD) {
            File failViz = new File(VALIDATION_PATH + "/FAIL_" + checkName + ".png");
            res.storeVisualization(failViz);
            throw new Exception(
                    String.format("Visual check of '%s' failed; similarity match was only %f, and below the threshold of %f. Visualization written to %s.",
                            checkName, res.getScore(), MATCH_THRESHOLD, failViz.getAbsolutePath()));
        }

        System.out.printf("Visual check of '%s' passed; similarity match was %f%n",
                checkName, res.getScore());
    }

}
