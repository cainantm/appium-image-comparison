import io.appium.java_client.AppiumBy;
import io.appium.java_client.Setting;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;

public class ImageBasedFunctionalTest extends BaseTest{

    private WebDriverWait wait;

    @Override
    protected DesiredCapabilities getCaps() throws URISyntaxException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("appium:platformName", "Android");
        caps.setCapability("appium:deviceName", "Android Emulator");
        caps.setCapability("appium:automationName", "UiAutomator2");
//        caps.setCapability("appium:appPackage", "com.sm.calculateme");
//        caps.setCapability("appium:appActivity", "com.unity3d.player.UnityPlayerActivity");
        caps.setCapability("appium:app", getResource("apps/calculator.apk").toString());
        caps.setCapability("appium:appWaitActivity", "com.unity3d.player.UnityPlayerActivity");
        caps.setCapability("appium:mipegScreenshotUrl", "http://192.168.0.251:8080/stream.mjpeg");

        return caps;
    }

    @Test
    public void testCalculatorQuestGame() throws Exception {
        driver.setSetting(Setting.IMAGE_MATCH_THRESHOLD, 0.85);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        navToFirstLevel();
        playLevel(1);
        playLevel(2);

        try{
            Thread.sleep(3000);
        } catch (Exception ex){

        }
    }

    private WebElement waitForImage(WebDriverWait wait, String image){
        return wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.image(image)));
    }

    private void actionDelay(){
        try{
            Thread.sleep(10000);
        } catch (Exception ex){
            ex.getMessage();
        }
    }

    private void clickImage(String image){
        waitForImage(wait, image).click();
        actionDelay();
    }

    private void navToFirstLevel() throws Exception {
        final String greenBtn = getReferenceImageB64("green-button.png");
        final String settings = getReferenceImageB64("settings.png");

        boolean settingVisible = false;
        int count = 0, maxCount=20;

        while(!settingVisible && count < maxCount){
            count+=1;
            try{
                clickImage(greenBtn);
            } catch (TimeoutException skip){
                continue;
            }

            try{
                driver.findElement(AppiumBy.image(settings));
                settingVisible = true;
            } catch (NoSuchElementException ex){

            }
        }

        if (count >= maxCount){
            throw new Exception("Could not navigate to first level");
        }
    }

    private void playLevel(int level) throws Exception {
        final String plus1 = getReferenceImageB64("plus-1.png");
        final String plus2 = getReferenceImageB64("plus-2.png");
        final String plus3 = getReferenceImageB64("plus-3.png");
        final String okButton = getReferenceImageB64("ok.png");
        final String greenButton = getReferenceImageB64("green-button.png");

        WebElement el;

        switch(level) {
            case 1:
                el = waitForImage(wait, plus1);
                el.click(); actionDelay();
                el.click(); actionDelay();
                break;
            case 2:
                el = waitForImage(wait, plus3);
                el.click(); actionDelay();
                el.click(); actionDelay();
                clickImage(plus2);
                break;
            default:
                throw new Exception("Don't know how to play that level yet");
        }

        clickImage(okButton);

        clickImage(greenButton);
    }
}
