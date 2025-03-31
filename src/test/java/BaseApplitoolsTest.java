import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.appium.Eyes;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.After;
import org.junit.Before;

public class BaseApplitoolsTest extends BaseTest{

    Eyes eyes;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        Dotenv dotenv = Dotenv.load();
        eyes = new Eyes();
        eyes.setLogHandler(new StdoutLogHandler());
        eyes.setApiKey(dotenv.get("APPLITOOLS_API_KEY"));
    }

    @Override
    @After
    public void tearDown() {
        eyes.abortIfNotClosed();
        super.tearDown();
    }

}
