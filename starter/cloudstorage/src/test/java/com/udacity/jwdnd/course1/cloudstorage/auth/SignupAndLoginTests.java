package com.udacity.jwdnd.course1.cloudstorage.auth;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SignupAndLoginTests {



    // this annotation will inject the random port that was chosen
    // into the port field for use in the tests
    @LocalServerPort
    private Integer port;

    private String baseURL = "http://localhost:";

    private static WebDriver driver;
    private LoginPage loginPage;
    private SignupPage signupPage;

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

    @Test
    @Order(1)
    void testSignUp() {
        String firstname = "George";
        String lastname = "Doe";
        String username = "gdoe1234";
        String password = "MyMyHomie1!";

        driver.get(baseURL + port + "/signup");
        signupPage = new SignupPage(driver);

        // fill out and submit the form
//        signupPage.typeFirstName(firstname)
//                    .typeLastName(lastname)
//                    .typeUsername(username)
//                    .typePassword(password)
//                    .clickSubmit();

        signupPage.signup(firstname, lastname, username, password);

        // wait for the success message to be visible
        By successMsg = By.id("success-msg");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        wait.until(ExpectedConditions.visibilityOfElementLocated(successMsg));

        boolean successIsDisplayed = driver.findElement(successMsg).isDisplayed();
        assertTrue(successIsDisplayed, "\n + The success message was not displayed.\n");

        // wait for login link to be visible
        By loginLink = By.id("login-link");
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
        wait1.until(ExpectedConditions.visibilityOfElementLocated(loginLink));

        // click the login link
        driver.findElement(loginLink).click();

        // wait for the login page to load
        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait2.until(ExpectedConditions.titleIs("Login"));
        assertEquals("Login", driver.getTitle());


//        driver.get(baseURL + port + "/login");
        loginPage = new LoginPage(driver);

         loginPage.login(username, password);
//        loginPage.typeUsername(username)
//                .typePassword(password)
//                .clickSubmit();

        // wait for the home page to load
        WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(1));
        wait3.until(ExpectedConditions.titleIs("Super Duper Drive"));

        assertEquals("Super Duper Drive", driver.getTitle());
    }

}
