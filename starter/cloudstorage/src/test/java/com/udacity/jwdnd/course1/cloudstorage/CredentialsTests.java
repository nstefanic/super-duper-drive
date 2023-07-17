package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.auth.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.auth.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CredentialsTests {

    // this annotation will inject the random port that was chosen
    // into the port field for use in the tests
    @LocalServerPort
    private Integer port;

    private final String baseURL = "http://localhost:";

    private static WebDriver driver;
    private HomePage homePage;
    private ResultPage resultPage;
    private final By successDiv = By.id("success");
    private final By generalErrorHeading = By.id("error");

    private final String url = "www.google.com";
    private final String username = "user";
    private final String password = "TerribleP@ssword0";

    private final String newUrl = "www.yahoo.com";
    private final String newUsername = "newUser";
    private final String newPassword = "NewP@ssword1";

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterAll
    public static void afterAll() throws InterruptedException {
        Thread.sleep(5000);
        driver.quit();
    }

//    ********** Happy Path Tests **********
    @Order(1)
    @Test
    @DisplayName("Test adding a credential")
    public void testAddCredential() {
        signUpAndLogin();

//        GIVEN
//        credentials properties are set above as class variables

//        WHEN
        goToHomePageAndAddCredential(url, username, password);

//        THEN
//            #1: check for success
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(successDiv));
        List<WebElement> success = driver.findElements(successDiv);

        assertTrue(success.size() > 0);

//            return to home page
        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();

//            click on credentials tab
        homePage.goToCredentialsTab();

//            #2: check for credential
        assertEquals(1, homePage.credentialsList.size());
    }


    @Test
    @Order(2)
    @DisplayName("Test editing previous credential")
//    NOTE: this test relies on the previous test to pass
    public void testEditCredential() {
//        signUpAndLogin();


//        GIVEN
//        credentials properties are same as first test (see class variables)


//        WHEN
//            notes tab should already be open (from activeTab)
        homePage = new HomePage(driver);
//        homePage.goToCredentialsTab();

//            check for credential
        assertEquals(1, homePage.credentialsList.size());

//            click on edit credential button for first credential
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.firstCredentialEditButton));

        homePage.openFirstCredentialToEdit();

//            edit credential
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.credentialUrlInput));
        homePage.editFirstCredential(newUrl, newUsername, newPassword);


//        THEN
//            #1: check for success
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(successDiv));
        List<WebElement> success = driver.findElements(successDiv);
        assertTrue(success.size() > 0);


//            return to home page and check for edited credential
        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();

//        homePage.goToCredentialsTab();
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.addCredentialButton));

//            #2: check url and username are edited
        assertEquals(newUrl, homePage.firstCredentialUrl.getText());
        assertEquals(newUsername, homePage.firstCredentialUsername.getText());
//            #3: check password is encrypted
        assertNotEquals(newPassword, homePage.firstCredentialPassword.getText());

    }


    @Test
    @Order(3)
    @DisplayName("Test deleting previous credential")
//    NOTE: this test relies on the previous test to pass
    public void testDeleteCredential() {

//        GIVEN
//        credentials properties are same as first test (see class variables)


//        WHEN
//            delete credential
        homePage = new HomePage(driver);
//        homePage.goToCredentialsTab();
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.firstCredentialDeleteButton));
        homePage.deleteFirstCredential();

//        THEN
//            #1: check for success
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(successDiv));
        List<WebElement> success = driver.findElements(successDiv);
        assertTrue(success.size() > 0);


//            return to home page and check for deleted credential
        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();
        homePage.goToCredentialsTab();
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.addCredentialButton));

//            #2: check credential is no longer displayed
        assertEquals(0, homePage.credentialsList.size());
    }


//    ************ ERROR TESTS ************

    @Test
    @Order(4)
    @DisplayName("Test adding a credential with blank username")
//    NOTE: assumes there are currently no stored credentials
//    because test #3 deletes the credential
    public void testAddCredentialWithBlankUsername() {
//        signUpAndLogin();

//        GIVEN
//        credentials properties are set above as class variables
//        NOTE: an empty username will not allow the submit button to be clicked
//        because of required attribute, which is why blank is tested instead.
        String blankUsername = " ";

//        WHEN
        goToHomePageAndAddCredential(url, blankUsername, password);

//        THEN
//            #1: check for error
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(generalErrorHeading));
        List<WebElement> error = driver.findElements(generalErrorHeading);
        assertTrue(error.size() > 0);

//            return to home page
        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();

//            click on credentials tab
//        homePage.goToCredentialsTab();

//            #2: check for credential
        assertEquals(0, homePage.credentialsList.size());
    }

    @Test
    @Order(5)
    @DisplayName("Test adding a credential with blank password")
//    NOTE: assumes there are currently no stored credentials
//    because test #3 deletes the credential
    public void testAddCredentialWithBlankPassword() {
//        signUpAndLogin();

//        GIVEN
//        credentials properties are set above as class variables
//        NOTE: an empty password will not allow the submit button to be clicked
//        because of required attribute, which is why blank is tested instead.
        String blankPassword = " ";

//        WHEN
        goToHomePageAndAddCredential(url, username, blankPassword);

//        THEN
//            #1: check for error
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(generalErrorHeading));
        List<WebElement> error = driver.findElements(generalErrorHeading);
        assertTrue(error.size() > 0);

//            return to home page
        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();

//            click on credentials tab
        homePage.goToCredentialsTab();

//            #2: check for credential
        assertEquals(0, homePage.credentialsList.size());
    }


    @Test
    @Order(6)
    @DisplayName("Test adding a credential with blank url")
//    NOTE: assumes there are currently no stored credentials
//    because test #3 deletes the credential
    public void testAddCredentialWithBlankUrl() {
//        signUpAndLogin();

//        GIVEN
//        credentials properties are set above as class variables
//        NOTE: an empty url will not allow the submit button to be clicked
//        because of required attribute, which is why blank is tested instead.
        String blankUrl = " ";

//        WHEN
        goToHomePageAndAddCredential(blankUrl, username, password);

//        THEN
//            #1: check for error
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(generalErrorHeading));
        List<WebElement> error = driver.findElements(generalErrorHeading);
        assertTrue(error.size() > 0);

//            return to home page
        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();

//            click on credentials tab
        homePage.goToCredentialsTab();

//            #2: check for credential
        assertEquals(0, homePage.credentialsList.size());
    }






//    ************ Helper Methods ************
    private void signUpAndLogin() {
        String firstname = "George";
        String lastname = "Doe";
        String username = "gdoe1234";
        String password = "MyMyHomie1!";

        driver.get(baseURL + port + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup(firstname, lastname, username, password);


        driver.get(baseURL + port + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

    }

    private void goToHomePageAndAddCredential(String url, String username, String password) {

//        go to home page
        driver.get(baseURL + port + "/home");
        homePage = new HomePage(driver);

//            click on credentials tab
        homePage.goToCredentialsTab();
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.addCredentialButton));

//            click on add credential button
        homePage.clickAddCredentialButton();

//            add credential
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.credentialUrlInput));
        homePage.addCredential(url, username, password);

    }

}
