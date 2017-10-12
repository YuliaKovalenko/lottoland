package common.util;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestBase {

    protected WebDriver driver;
    private Logger logger = Logger.getLogger(TestBase.class);
    private static final String PATH_TO_DRIVERS = "src/test/resources/Driver/";
    private static final String WINDOWS_OUTPUT_FOLDER = System.getProperty("user.home") + File.separator + "test";

    @BeforeTest(alwaysRun = true)
    public void setBrowser() throws IOException {
        driver = getDriver(PropertyLoader.loadProperty("browser"));
    }

    @AfterTest(alwaysRun = true)
    public void disconnect() throws IOException {
        driverQuit();
    }

    @AfterMethod(alwaysRun = true)
    public void finalizeTest(Method method, ITestResult result) throws IOException {
        try {
            closeOpenedWindow();
            logger.info("\n\n:::\n End of Test:\n" + method.getName() + "\n:::\n");
        }catch (NullPointerException npe){
            logger.error("NPE. Try to relaunch Browser");
        }
        takeScreenshot(result);
    }

    private void driverQuit() {
        logger.info("Entering AfterClass(common.common.util.TestBase)\n Teardown - Check null");
        if (driver != null) {
            logger.info("Closing browser after TestClass");
            driver.quit();
        } else {
            logger.error("Driver is null at AfterClass (common.common.util.TestBase)");
        }
        logger.info("Teardown - Exiting");
    }

    private WebDriver getDriver(String browserType) {
        DesiredCapabilities capability;

        if (browserType.equals("firefox")) {
            //Set Capability
            capability = DesiredCapabilities.firefox();
            capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
            capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
            capability.setCapability("marionette", true);
            String os = System.getProperty("os.name").toLowerCase().substring(0, 3);
            logger.info("System operation is : " + System.getProperty("os.name"));
            String geckoBinary = os.equals("win") ? "src\\test\\resources\\Driver\\ff\\geckodriver.exe" : "src/test/resources/Driver/ff/geckodriver";
            System.setProperty("webdriver.gecko.driver", geckoBinary);

            //Set Profile
            FirefoxProfile profile = new FirefoxProfile();
            profile.setEnableNativeEvents(false);
            profile.setPreference("browser.helperApps.alwaysAsk.force", false);
            profile.setPreference("browser.download.folderList", 2);
            // profile.setPreference("webdriver_firefox_port", 7054);
            profile.setPreference("browser.download.manager.showWenStarting", false);
            profile.setPreference("browser.download.manager.focusWhenStarting", false);
            profile.setPreference("browser.download.manager.useWindow", false);
            profile.setPreference("browser.download.manager.showAlertOnComplete", false);
            profile.setPreference("browser.download.manager.closeWhenDone", true);
            profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/vnd.ms-powerpoint,application/iopexport_bib,application/iopexport_ris");
            //Get Driver
            driver = new FirefoxDriver(profile);

        } else if (browserType.equals("chrome")) {
            String chromeBinary;
            capability = DesiredCapabilities.chrome();
            capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
            String os = System.getProperty("os.name").toLowerCase().substring(0, 3);
            logger.info("System operation is : " + System.getProperty("os.name"));
            if (os.equals("win")) {
                chromeBinary = PATH_TO_DRIVERS + "chrome/chromedriver.exe";
            } else if (os.equals("mac")) {
                chromeBinary = PATH_TO_DRIVERS + "chrome/chromedriver";
                File file = new File(chromeBinary);
                file.setExecutable(true, false);
            } else {
                chromeBinary = PATH_TO_DRIVERS + "chrome/chromedriverLinux";
                File file = new File(chromeBinary);
                file.setExecutable(true, true);
            }
            logger.info("Used path to the Chrome binary:\n" + chromeBinary);
            System.setProperty("webdriver.chrome.driver", chromeBinary);
            System.setProperty("DISPLAY", ":19");
            System.setProperty("webdriver.chrome.driver", chromeBinary);
            ChromeOptions options = new ChromeOptions();
            options.addArguments("chrome.switches", "--disable-extensions");
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("download.default_directory", WINDOWS_OUTPUT_FOLDER);

            options.setExperimentalOption("prefs", prefs);

            driver = new ChromeDriver(options);
        }
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return driver;
    }

    private void takeScreenshot(ITestResult result) throws IOException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
        String methodName = result.getName();
        if (!result.isSuccess()){
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(source, new File("./screenshots/" + methodName + "_" + formatDate.format(calendar.getTime()) + ".png"));
                logger.error("ERROR\n\nPAGE SOURCE of FAILED TEST\n\n*** START ***\n\n" + driver.getPageSource() + "\n\n*** END ***\n\n");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void closeOpenedWindow() {
        while (driver.getWindowHandles().size() > 1) {
            logger.warn("There are: [" + driver.getWindowHandles().size() + "] windows");
            logger.warn("Closing window with title: \n" + driver.getTitle());
            driver.close();
            switchToOpenedWindow();
        }
    }

    private void switchToOpenedWindow() {
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
            logger.warn("Switch to browser window with title: \n" + driver.getTitle());
        }
    }
}