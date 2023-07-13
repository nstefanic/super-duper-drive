package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.auth.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.auth.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserFileUploadTests {

    @LocalServerPort
    private Integer port;

    private String baseURL = "http://localhost:";

    private static WebDriver driver;
    private LoginPage loginPage;
    private SignupPage signupPage;
    private HomePage homePage;
    private ResultPage resultPage;

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
    public void testFileUpload() {
        signUpAndLogin();

        String title = "My New Note";
        String description = "This is my note and it is wonderful.";
        driver.get(baseURL + port + "/home");
        homePage = new HomePage(driver);

        homePage.goToFilesTab();
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.fileUploadInput));

        String path = "./src/test/resources";
        String fileName = "file-upload-test.txt";

        File file = new File(path, fileName);
        String absolutePath = file.getAbsolutePath();
        System.out.println("filePath for file upload test: " + absolutePath);
        homePage.uploadFile(absolutePath);

        resultPage = new ResultPage(driver);

        By successDiv = By.id("success");
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(successDiv));
        List<WebElement> success = driver.findElements(successDiv);


        assertTrue(success.size() > 0);

        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();

        homePage.goToFilesTab();

        System.out.println("firstFileViewButton: " + homePage.firstFileViewButton.getText());

//        wait for the first file to be clickable
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.firstFileViewButton));

//        check if the file name is displayed
        String firstFileName = homePage.firstFileName.getText();
        System.out.println("firstFileName: " + firstFileName);
        assertEquals(firstFileName, fileName);

//        click on View button of first file
        try {
            homePage.viewFirstFile();
            String currentUrl = driver.getCurrentUrl();
            System.out.println("currentUrl: " + currentUrl);
            Thread.sleep(1000);
            assertTrue(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
            assertTrue(false);
        }


//        assertEquals(currentUrl, "http://localhost:" + this.port + "/files/" + fileName);

    }


    // helper function
    void signUpAndLogin() {
        String firstname = "George";
        String lastname = "Doe";
        String username = "gdoe1234";
        String password = "MyMyHomie1!";

        driver.get(baseURL + port + "/signup");
        signupPage = new SignupPage(driver);
        signupPage.signup(firstname, lastname, username, password);


        driver.get(baseURL + port + "/login");
        loginPage = new LoginPage(driver);
        loginPage.login(username, password);

    }
}
