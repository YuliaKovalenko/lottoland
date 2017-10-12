import common.entities.User;
import common.util.TestBase;
import content.RegistrationPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static content.PageObject.APP_URL;
import static content.PageObject.REGISTRATION_PATH;
import static org.testng.Assert.assertEquals;

public class RegistrationTest extends TestBase {

    private RegistrationPage registrationPage;
    private List<User> nonRegisteredUsers = new ArrayList<User>(Arrays.asList(User.values()));

    @BeforeMethod
    public void setupPage() {
        registrationPage = new RegistrationPage(driver);
        registrationPage.goToRegistrationPage();
    }

    @Test(priority = -10)
    public void registrationPageUrlTest() {
        assertEquals(driver.getCurrentUrl(), APP_URL + REGISTRATION_PATH, "Registration page redirect to wrong url");
    }

    @DataProvider(name = "registration data")
    public Object[][] registrationUsers() {
        ArrayList<User> registrationUsers = registrationPage.getRandomUser(5);
        return new Object[][]{
                {registrationUsers.get(0), "Dance", RandomStringUtils.randomNumeric(10), registrationUsers.get(0).getName(), RandomStringUtils.randomAlphanumeric(7) + "@gmail.com", "password"},
                {registrationUsers.get(1), "Reading", RandomStringUtils.randomNumeric(10), registrationUsers.get(1).getName(), RandomStringUtils.randomAlphanumeric(7) + "@gmail.com", "password"},
                {registrationUsers.get(2), "Cricket ", RandomStringUtils.randomNumeric(10), registrationUsers.get(2).getName(), RandomStringUtils.randomAlphanumeric(7) + "@gmail.com", "password"},
                {registrationUsers.get(3), "Dance", RandomStringUtils.randomNumeric(10), registrationUsers.get(3).getName(), RandomStringUtils.randomAlphanumeric(7) + "@gmail.com", "password"},
                {registrationUsers.get(4), "Reading", RandomStringUtils.randomNumeric(10), registrationUsers.get(4).getName(), RandomStringUtils.randomAlphanumeric(7) + "@gmail.com", "password"},
        };
    }

    @Test(dataProvider = "registration data", priority = 10)
    public void registrationTest(User user, String hobby, String phone, String username, String email, String password) {
        registrationPage.registerUserWithRequiredField(user, hobby, phone, username, email, password);

        nonRegisteredUsers.remove(user);
        registrationPage.printNonRegisteredUsers(nonRegisteredUsers);

        assertEquals(registrationPage.notificationMessage.getText(), registrationPage.successMessage);
    }
}