package content;

import common.entities.User;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RegistrationPage extends PageObject {

    private Logger logger = Logger.getLogger(RegistrationPage.class);
    public RegistrationPage(WebDriver driver){
        init(driver);
    }

    @FindBy(id = "name_3_firstname")
    private WebElement firstNameInput;

    @FindBy(id = "name_3_lastname")
    private WebElement lastNameInput;

    @FindBy(xpath = ".//input[@name='checkbox_5[]']")
    private WebElement hobbyCheckbox;

    @FindBy(id = "phone_9")
    private WebElement phoneInput;

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "email_1")
    private WebElement emailInput;

    @FindBy(id = "password_2")
    private WebElement passwordInput;

    @FindBy(id = "confirm_password_password_2")
    private WebElement confirmPassword;

    @FindBy(xpath = ".//input[@name='pie_submit']")
    private WebElement submitButton;

    @FindBy(className = "piereg_message")
    public WebElement notificationMessage;

    public void goToRegistrationPage(){
            driver.get(APP_URL + REGISTRATION_PATH);
            basePage.waitingForPageIsLoaded();
    }

    public void registerUserWithRequiredField(User user, String hobby, String phone, String username, String email, String password) {
        logger.info("Registration user " + user.getName());
        firstNameInput.sendKeys(user.getFirstName());
        lastNameInput.sendKeys(user.getLastName());
        driver.findElement(By.xpath(".//input[@name='checkbox_5[]'][@value='" + hobby.toLowerCase() + "']")).click();
        phoneInput.sendKeys(phone);
        usernameInput.sendKeys(username);
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        confirmPassword.sendKeys(password);
        submitButton.click();
    }

    public ArrayList<User> getRandomUser(int numberUsers) {
        ArrayList<User> randomUsers = new ArrayList<>();
        List<User> users = new ArrayList<User>(Arrays.asList(User.values()));
        for (int i = 0; i < numberUsers; i++) {
            Random random = new Random();
            int pick = random.nextInt(users.size());
            randomUsers.add(users.get(pick));
            users.remove(pick);
        }
        return randomUsers;
    }

    public void printNonRegisteredUsers(List<User> nonRegisteredUsers){
        ArrayList<String> nonRegisteredUserName = new ArrayList<>();
        for (User nonRegisteredUser : nonRegisteredUsers) {
            nonRegisteredUserName.add(nonRegisteredUser.getName());
        }
        System.out.println(nonRegisteredUserName);
    }

    public String successMessage = "Thank you for your registration";
}
