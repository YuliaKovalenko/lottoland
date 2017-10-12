package content;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.testng.Assert.assertTrue;

public class BasePage {

    private Logger logger = Logger.getLogger(BasePage.class);
    private WebDriver driver;

    public BasePage(WebDriver driver){
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void waitingForPageIsLoaded(){
        logger.info("\n\nLoading page: \n" + driver.getCurrentUrl());
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
        assertTrue(isNotErrorPage());
    }

    private boolean isNotErrorPage(){
        boolean result = true;
        if(
                driver.getPageSource().contains("We are sorry, that page has not been found")||
                driver.getTitle().contains("500 Internal Server Error")||
                driver.getTitle().contains("Internal Error Occurred")||
                driver.getTitle().contains("Wrap Long Lines")||
                driver.getTitle().contains("Service Unavailable")||
                driver.getTitle().contains("Error!")||
                driver.getPageSource().contains("500 Internal Server Error")||
                driver.getTitle().contains("Page Not Found")||
                driver.getPageSource().contains("Authentication required")||
                driver.getPageSource().contains("Internal Error Occurred")||
                driver.getPageSource().contains("Connection was interrupted")
        ){
            result = false;
            logger.error("Error page: " + driver.getTitle());
        }
        return result;
    }

    protected void maximizeWindow(){
        Dimension expectedWindowSize = new Dimension(1700, 1200);
        logger.info("Maximize browser window size");
        driver.manage().window().setSize(expectedWindowSize);
    }
    public void refreshPage(){
        driver.navigate().refresh();
        waitingForPageIsLoaded();
    }
    public void clickEsc(){
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ESCAPE).perform();
        waitingForPageIsLoaded();
    }
}
