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

    private static String baseURL = "http://localhost:";

    private static WebDriver driver;
    private HomePage homePage;
    private ResultPage resultPage;
    private By successDiv = By.id("success");

    String url = "www.google.com";
    String username = "user";
    String password = "TerribleP@ssword0";

    String newUrl = "www.yahoo.com";
    String newUsername = "newUser";
    String newPassword = "NewP@ssword1";

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

    @BeforeEach
    public void beforeEach() {
    }

    @Order(1)
    @Test
    @DisplayName("Test adding a credential")
    public void testAddCredential() {
        signUpAndLogin();

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

//            check for success
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(successDiv));
        List<WebElement> success = driver.findElements(successDiv);

        assertTrue(success.size() > 0);

//            return to home page
        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();

//            click on credentials tab
        homePage.goToCredentialsTab();

//            check for credential
        assertEquals(1, homePage.credentialsList.size());
    }

    @Order(2)
    @Test
    @DisplayName("Test editing previous credential")
    public void testEditCredential() {

//            click on notes tab
        homePage = new HomePage(driver);
        homePage.goToCredentialsTab();

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


//            check for success
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(successDiv));
        List<WebElement> success = driver.findElements(successDiv);
        assertTrue(success.size() > 0);


//            return to home page and check for edited credential
        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();

        homePage.goToCredentialsTab();
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.addCredentialButton));

//        check url and username are edited
        assertEquals(newUrl, homePage.firstCredentialUrl.getText());
        assertEquals(newUsername, homePage.firstCredentialUsername.getText());
//           check password is encrypted
        assertNotEquals(newPassword, homePage.firstCredentialPassword.getText());

    }

    @Order(3)
    @Test
    @DisplayName("Test deleting previous credential")
    public void testDeleteCredential() {

//            delete credential
        homePage = new HomePage(driver);
        homePage.goToCredentialsTab();
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.firstCredentialDeleteButton));
        homePage.deleteFirstCredential();

//            check for success
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(successDiv));
        List<WebElement> success = driver.findElements(successDiv);
        assertTrue(success.size() > 0);


//            return to home page and check for deleted credential
        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();

//            check credential is no longer displayed
        homePage.goToCredentialsTab();
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.addCredentialButton));

        assertEquals(0, homePage.credentialsList.size());
    }


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

}
