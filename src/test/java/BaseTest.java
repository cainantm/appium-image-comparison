import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class BaseTest {

    AndroidDriver driver;

    protected DesiredCapabilities getCaps() throws Exception {
        throw new Exception("Falta implementar as Capabilities");
    }

    @Before
    public void setUp() throws Exception {
        URL server = new URL("http://127.0.0.1:4723");
        driver = new AndroidDriver(server, getCaps());
    }

    @After
    public void tearDown(){
        if(driver != null){
            driver.quit();
        }
    }

    Path getResource(String fileName) throws URISyntaxException {
        URL refImgUrl = getClass().getClassLoader().getResource(fileName);
        return Paths.get(refImgUrl.toURI()).toFile().toPath();
    }

    private String getResourceB64(String fileName) throws IOException, URISyntaxException {
        Path refImgPath = getResource(fileName);
        return Base64.getEncoder().encodeToString(Files.readAllBytes(refImgPath));
    }

    String getReferenceImageB64(String fileName) throws IOException, URISyntaxException {
        return getResourceB64("images/" + fileName);
    }

}
