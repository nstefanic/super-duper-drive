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

import java.io.File;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserFileUploadTests {

    @LocalServerPort
    private Integer port;
    private final String baseURL = "http://localhost:";
    private static WebDriver driver;
    private HomePage homePage;
    private ResultPage resultPage;
    private final String filePath = "src/test/resources";
    private final String fileName = "test.txt";
    private final By successDiv = By.id("success");
    private final By generalErrorHeading = By.id("error");

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
    @DisplayName("Test that a file can be uploaded")
    public void testFileUpload() {
        signUpAndLogin();

//        GIVEN
//        filepath and filename are provided as class variables

        homePage = new HomePage(driver);
        homePage.goToNotesTab();
        homePage.goToCredentialsTab();
        homePage.goToFilesTab();

//        WHEN
        goToHomePageAndUploadFile(filePath, fileName);

        resultPage = new ResultPage(driver);

        System.out.println(driver.getCurrentUrl());

//        wait for success message to be displayed
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(successDiv));
        List<WebElement> success = driver.findElements(successDiv);

//        THEN
//        check if success message is displayed
        assertTrue(success.size() > 0);

        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();
    }

    @Test
    @Order(2)
    @DisplayName("Test that previously uploaded file can be viewed")
    public void testViewPreviouslyUploadedFile() {
//        signUpAndLogin();

//        GIVEN
//        filepath and filename is the same as in previous test (see class variables)


//        WHEN
        homePage = new HomePage(driver);
//        homePage.goToFilesTab();

        System.out.println("firstFileViewButton: " + homePage.firstFileViewButton.getText());

//        wait for the first file to be clickable
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.firstFileViewButton));

//        check if the file name is displayed
        String firstFileName = homePage.firstFileName.getText();
        System.out.println("firstFileName: " + firstFileName);
        assertEquals(firstFileName, fileName);

//        THEN
//        click on View button of first file
//        if viewFirstFile() errors then test will fail
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
//    this test is the third in a row because it depends on the previous two tests
    public void testDownloadFile() {
//        signUpAndLogin();


//        GIVEN
//        filepath and filename is the same as in first test (see class variables)

//        WHEN
        homePage = new HomePage(driver);
//        homePage.goToFilesTab();

//        wait for the first file to be clickable
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.firstFileDownloadButton));

//        THEN
//        click on Download button of first file
//        if downloadFirstFile() errors then test will fail
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
//    this test is the fourth in a row because it depends on the previous three tests
    public void testDeleteFile() {
//        signUpAndLogin();

//        GIVEN
//        filepath and filename is the same as in first test (see class variables)


//        WHEN
        homePage = new HomePage(driver);
//        homePage.goToFilesTab();

//        wait for the first file to be clickable
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(homePage.firstFileDeleteButton));

        homePage.deleteFirstFile();

//        wait for success message to be displayed
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(successDiv));
        List<WebElement> success = driver.findElements(successDiv);

//        THEN
//        check if success message is displayed
        assertTrue(success.size() > 0);


        resultPage = new ResultPage(driver);
        resultPage.returnToHomePage();
    }

    @Test
    @Order(5)
    @DisplayName("Test that a file can be uploaded with the same name")
//    this test can be run independently of the tests above
    public void testFileUploadWithSameName() {
//        signUpAndLogin();

//        GIVEN
//        filepath and filename are provided through the class variables


//        WHEN
//        upload first file
        goToHomePageAndUploadFile(filePath, fileName);
//        check if first file was sucessfully uploaded
        resultPage = new ResultPage(driver);
        assertTrue(resultPage.successHeading.isDisplayed());

//        upload second file with same name
        goToHomePageAndUploadFile(filePath, fileName);


//        wait for error message to be displayed
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(generalErrorHeading));

//        THEN
//        check if error message is displayed (it should be)
        assertTrue(resultPage.generalErrorHeading.isDisplayed());

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
        assertTrue(resultPage.generalErrorHeading.isDisplayed());

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
        assertTrue(resultPage.generalErrorHeading.isDisplayed());

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
        assertTrue(resultPage.generalErrorHeading.isDisplayed());

    }

//    @MockBean
//    private FileService fileService;

//    @Test
//    @Order(10)
//    @DisplayName("ERROR Test database error")
//    @Disabled
//    public void testDatabaseError() {
//
//        signUpAndLogin();
//
////       GIVEN
//        String filePath = "./src/test/resources";
//        String fileName = "file-upload-test.txt";
//
////        WHEN
////        go to home page
//        driver.get(baseURL + port + "/home");
//        homePage = new HomePage(driver);
//
////        go to files tab
//        homePage.goToFilesTab();
//        new WebDriverWait(driver, Duration.ofSeconds(1))
//                .until(ExpectedConditions.elementToBeClickable(homePage.fileUploadInput));
//
////        upload file
//        File file = new File(filePath, fileName);
//        String absolutePath = file.getAbsolutePath();
//        System.out.println("filePath for file upload test: " + absolutePath);
//
////        THIS IS THE ACTION FOR WHICH I WANT TO MOCK A DATABASE RESPONSE
//        homePage.uploadFile(absolutePath);
//
//
////        Everything below here makes not sense at the moment
//        UserFile userFile = new UserFile(
//                fileName,
//                "text/plain",
//                "1000",
//                1,
//                new byte[0]
//        );
//
////        when(fileService.uploadFile(userFile)).thenReturn(-1);
//
//
////        THEN
//        assertThrows(Exception.class, () -> fileService.uploadFile(userFile));
//        resultPage = new ResultPage(driver);
////        *** THIS IS WHAT I WANT TO TEST ***
//        assertTrue(resultPage.generalErrorHeading.isDisplayed());
//
//    }



    // helper function
    void signUpAndLogin() {
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
