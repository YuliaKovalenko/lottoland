package content;

import common.util.PropertyLoader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class PageObject {

    WebDriver driver;
    BasePage basePage;

    public final static String APP_URL = PropertyLoader.loadProperty("app_url");
    public final static String REGISTRATION_PATH = "registration/";

    void init(WebDriver driver){
        basePage = new BasePage(driver);
        PageFactory.initElements(driver, this);
        this.driver = driver;
        basePage.maximizeWindow();
    }
}
