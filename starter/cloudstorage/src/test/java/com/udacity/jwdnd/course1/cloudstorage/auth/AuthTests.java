package com.udacity.jwdnd.course1.cloudstorage.auth;

import com.udacity.jwdnd.course1.cloudstorage.HomePage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTests {



    // this annotation will inject the random port that was chosen
    // into the port field for use in the tests
    @LocalServerPort
    private Integer port;

    private final String baseURL = "http://localhost:";

    private static WebDriver driver;
    private LoginPage loginPage;

    private final String firstname = "George";
    private final String lastname = "Doe";
    private final String username = "gdoe1234";
    private final String password = "MyMyHomie1!";

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

    @Test
    @Order(1)
    void testSignUp() {

//        GIVEN
//        all signup strings are class variables (see above)

//        WHEN
        driver.get(baseURL + port + "/signup");
        SignupPage signupPage = new SignupPage(driver);

//        fill out and submit the form
        signupPage.signup(firstname, lastname, username, password);
/*
        signupPage.typeFirstName(firstname)
                    .typeLastName(lastname)
                    .typeUsername(username)
                    .typePassword(password)
                    .clickSubmit();
*/

        // wait for the login page to load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        wait.until(ExpectedConditions.titleIs("Login"));
        assertEquals("Login", driver.getTitle());

        // wait for the success message to be visible
        By successMsg = By.id("success-msg");
        wait.until(ExpectedConditions.visibilityOfElementLocated(successMsg));

        boolean successIsDisplayed = driver.findElement(successMsg).isDisplayed();
        assertTrue(successIsDisplayed, "\n + The success message was not displayed.\n");


    }

    @Test
    @Order(2)
    @DisplayName("Test Login")
//    NOTE:  This test will fail if the previous test is not run first
    void testLogin() {

//        GIVEN
//        all login strings are class variables (see above)
//        assume signup was successful (see previous test)

//        WHEN
        loginPage = new LoginPage(driver);
        loginPage.login(username, password);
/*
        loginPage.typeUsername(username)
                .typePassword(password)
                .clickSubmit();
*/

//        THEN
//         wait for the home page to load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        wait.until(ExpectedConditions.titleIs("Super Duper Drive"));

        assertEquals("Super Duper Drive", driver.getTitle());
    }

    @Test
    @Order(3)
    @DisplayName("Test Logout")
//    NOTE:  This test will fail if the previous test is not run first
    void testLogout() {

//        GIVEN
//        assume login was successful (see previous test)

//        WHEN
        HomePage homePage = new HomePage(driver);
        homePage.logout();

        loginPage = new LoginPage(driver);

//        THEN
//        Should re-direct to login page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        wait.until(ExpectedConditions.titleIs("Login"));
//        check that we are at the login page
        assertEquals("Login", driver.getTitle());
//        check that the logout message is displayed
        assertTrue(loginPage.loggedOutMessage.isDisplayed());

//        attempt to go to home page (should not be able to)
        driver.get(baseURL + port + "/home");
//        check that we are re-directed to login page
        assertEquals("Login", driver.getTitle());
    }

    @Test
    @Order(4)
    @DisplayName("Test Unauthorized Access")
    public void testUnauthorizedAccess() {

//        GIVEN
//        the user has not logged in

//        WHEN
//        attempt to go to home page (should not be able to)
        driver.get(baseURL + port + "/home");

//        THEN
//        check that we are re-directed to login page
        assertEquals("Login", driver.getTitle());
    }


}
