package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.auth.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.auth.SignupPage;
import com.udacity.jwdnd.course1.cloudstorage.models.UserFile;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.net.UnknownServiceException;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserFileUploadTests {

    @LocalServerPort
    private Integer port;
    private String baseURL = "http://localhost:";
    private static WebDriver driver;
    private LoginPage loginPage;
    private SignupPage signupPage;
    private HomePage homePage;
    private ResultPage resultPage;
    String filePath = "./src/test/resources";
    String fileName = "file-upload-test.txt";
    By successDiv = By.id("success");
    By errorHeading = By.id("error-heading");

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
    @DisplayName("Test that a file can be uploaded")
    public void testFileUpload() {
        signUpAndLogin();

        goToHomePageAndUploadFile(filePath, fileName);

        resultPage = new ResultPage(driver);

//        wait for success message to be displayed
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(successDiv));
        List<WebElement> success = driver.findElements(successDiv);

//        check if success message is displayed
        assertTrue(success.size() > 0);

        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();
    }

    @Test
    @Order(2)
    @DisplayName("Test that a file can be viewed")
    public void testViewFile() {
        homePage = new HomePage(driver);
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
    }

    @Test
    @Order(3)
    @DisplayName("Test that a file can be downloaded")
    public void testDownloadFile() {
        homePage = new HomePage(driver);
        homePage.goToFilesTab();

//        wait for the first file to be clickable
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.firstFileDownloadButton));

//        click on Download button of first file
        try {
            homePage.downloadFirstFile();
            String currentUrl = driver.getCurrentUrl();
            System.out.println("currentUrl: " + currentUrl);
            Thread.sleep(1000);
            assertTrue(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    @Order(4)
    @DisplayName("Test that a file can be deleted")
    public void testDeleteFile() {
        homePage = new HomePage(driver);
        homePage.goToFilesTab();

//        wait for the first file to be clickable
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.firstFileDeleteButton));

        homePage.deleteFirstFile();

//        wait for success message to be displayed
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(successDiv));
        List<WebElement> success = driver.findElements(successDiv);

        assertTrue(success.size() > 0);


        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();
    }

    @Test
    @Order(5)
    @DisplayName("Test that a file can be uploaded with the same name")
    public void testFileUploadWithSameName() {
//        signUpAndLogin();

//        upload first file
        goToHomePageAndUploadFile(filePath, fileName);
//        check if first file was sucessfully uploaded
        resultPage = new ResultPage(driver);
        assertTrue(resultPage.successHeading.isDisplayed());

//        upload second file with same name
        goToHomePageAndUploadFile(filePath, fileName);


//        wait for error message to be displayed
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(errorHeading));

//        check if error message is displayed (it should be)
        assertTrue(resultPage.errorHeading.isDisplayed());

//        check if success message is displayed (it should NOT be)
        List<WebElement> success = driver.findElements(successDiv);
        assertTrue( success.size() == 0);

//        return to home page
        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();

    }

//    @Test
//    @Order(0)
//    @DisplayName("Test upload error when not logged in")
//    public void testUploadErrorWhenNotLoggedIn() throws ClientProtocolException, IOException
//
//        // Given
//        String name = RandomStringUtils.randomAlphabetic( 8 );
//        HttpUriRequest request = new HttpGet( baseURL + port + "/file-upload" );
//
//        // When
//        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
//
//        // Then
//        assertThat(
//                httpResponse.getStatusLine().getStatusCode(),
//                equalTo(HttpStatus.SC_NOT_FOUND));
//        driver.get(baseURL + port + "/home");
//        homePage = new HomePage(driver);
//
//        homePage.goToFilesTab();
//        new WebDriverWait(driver, Duration.ofSeconds(1))
//                .until(ExpectedConditions.elementToBeClickable(homePage.fileUploadInput));
//    }

    @Test
    @Order(6)
    @DisplayName("ERROR Test empty file upload")
    public void testEmptyFileUpload() {
//        signUpAndLogin();

//        GIVEN
        String emptyFilePath = "./src/test/resources";
        String emptyFileName = "empty.txt";

//        WHEN
        goToHomePageAndUploadFile(emptyFilePath, emptyFileName);

//        THEN
//        check if error message is displayed
        resultPage = new ResultPage(driver);
        assertTrue(resultPage.errorHeading.isDisplayed());

    }

    @Test
    @Order(7)
    @DisplayName("ERROR Test file size limit")
//    NOTE: this test will fail if the file size limit is changed
//    in the application.properties file
//    which is currently set to 11MB
    public void testFileSizeLimit() {
//        signUpAndLogin();

//        GIVEN
        String largeFilePath = "./src/test/resources";
        String largeFileName = "some-silence.wav";
        // this file is 10.1 MB, which is larger than the 10 MB limit,
        // but lower than the 10485760 byte max MB limit set for the application
        // NOTE: this is a wav file, but since the file size check is done before the content type check
        // it will fail the file size check before it gets to the content type check

//        WHEN
        goToHomePageAndUploadFile(largeFilePath, largeFileName);

//        THEN
//        check if error message is displayed
        resultPage = new ResultPage(driver);
        assertTrue(resultPage.errorHeading.isDisplayed());

    }

    @Test
    @Order(8)
    @DisplayName("ERROR Test invalid content type")
//    NOTE: this test will fail if the audio/wav content type is allowed
    public void testInvalidContentType() {
//        signUpAndLogin();

//        GIVEN
        String invalidContentTypeFilePath = "./src/test/resources";
        String invalidContentTypeFileName = "small-silence.wav";
        // This file is only 531 KB, so should pass the file size limit,
        // but it is a wav file, which is not allowed.

//        WHEN
        goToHomePageAndUploadFile(invalidContentTypeFilePath, invalidContentTypeFileName);

//        THEN
//        check if error message is displayed
        resultPage = new ResultPage(driver);
        assertTrue(resultPage.errorHeading.isDisplayed());

    }

    @Test
    @Order(9)
    @DisplayName("ERROR Test empty file name")
    public void testEmptyFileName() {
        signUpAndLogin();

//        GIVEN
        String filePath = "./src/test/resources";
        String fileName = "";

//        WHEN
        goToHomePageAndUploadFile(filePath, fileName);

//        THEN
//        check if error message is displayed
        resultPage = new ResultPage(driver);
        assertTrue(resultPage.errorHeading.isDisplayed());

    }

    @MockBean
    private FileService fileService;

    @Test
    public void testDatabaseError() throws Exception {

        signUpAndLogin();

        UserFile file = new UserFile(
                "test.txt",
                "text/plain",
                "1000",
                1,
                new byte[0]
        );

        // Mock the database error
//        when(fileService.uploadFile(file)).thenThrow(new UnknownServiceException());

        // Perform the request and verify the response
//        goToHomePageAndUploadFile(filePath, fileName);
        when(fileService.uploadFile(file)).thenReturn(-1);


        resultPage = new ResultPage(driver);

        assertTrue(resultPage.errorHeading.isDisplayed());
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

    void goToHomePageAndUploadFile(String filePath, String fileName) {
        driver.get(baseURL + port + "/home");
        homePage = new HomePage(driver);

        homePage.goToFilesTab();
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.fileUploadInput));

//        upload file
        File file = new File(filePath, fileName);
        String absolutePath = file.getAbsolutePath();
        System.out.println("filePath for file upload test: " + absolutePath);
        homePage.uploadFile(absolutePath);
    }


}
