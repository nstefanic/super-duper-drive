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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NotesTests {

        // this annotation will inject the random port that was chosen
        // into the port field for use in the tests
        @LocalServerPort
        private Integer port;

        private static String baseURL = "http://localhost:";

        private static WebDriver driver;
        private HomePage homePage;
        private ResultPage resultPage;
        private By successDiv = By.id("success");

        String noteTitle = "My New Note";
        String noteDescription = "This is my note and it is wonderful.";
        String newNoteTitle = "My EDITED Title";
        String newNoteDescription = "My EDITED description is even better.";


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
        @DisplayName("Test adding a note")
        public void testAddNote() {
            signUpAndLogin();

            driver.get(baseURL + port + "/home");
            homePage = new HomePage(driver);

//            click on notes tab
            homePage.goToNotesTab();
            new WebDriverWait(driver, Duration.ofSeconds(1))
                    .until(ExpectedConditions.elementToBeClickable(homePage.addNoteButton));

//            click on add note button
            homePage.clickAddNoteButton();

//            add note
            new WebDriverWait(driver, Duration.ofSeconds(1))
                    .until(ExpectedConditions.elementToBeClickable(homePage.noteTitleInput));
            homePage.addNote(noteTitle, noteDescription);

//            check for success
            new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(successDiv));
            List<WebElement> success = driver.findElements(successDiv);

            assertTrue(success.size() > 0);

//            return to home page
            resultPage = new ResultPage(driver);
            resultPage.returnToHomePage();

//            click on notes tab
            homePage.goToNotesTab();

//            check for note
            assertEquals(1, homePage.notesList.size());
        }

        @Order(2)
        @Test
        @DisplayName("Test editing previous note")
        public void testEditNote() {

//            click on notes tab
            homePage = new HomePage(driver);
            homePage.goToNotesTab();

//            check for note
            assertEquals(1, homePage.notesList.size());

//            click on edit note button for first note
            new WebDriverWait(driver, Duration.ofSeconds(1))
                    .until(ExpectedConditions.elementToBeClickable(homePage.firstNoteEditButton));

            homePage.openFirstNoteToEdit();

//            edit note
            new WebDriverWait(driver, Duration.ofSeconds(1))
                    .until(ExpectedConditions.elementToBeClickable(homePage.noteTitleInput));
            homePage.editFirstNote(newNoteTitle, newNoteDescription);


//            check for success
            new WebDriverWait(driver, Duration.ofSeconds(1))
                    .until(ExpectedConditions.visibilityOfElementLocated(successDiv));
            List<WebElement> success = driver.findElements(successDiv);
            assertTrue(success.size() > 0);


//            return to home page and check for edited note
            resultPage = new ResultPage(driver);
            resultPage.returnToHomePage();

            homePage.goToNotesTab();
            new WebDriverWait(driver, Duration.ofSeconds(1))
                    .until(ExpectedConditions.elementToBeClickable(homePage.addNoteButton));

            assertEquals(newNoteTitle, homePage.firstNoteTitle.getText());
            assertEquals(newNoteDescription, homePage.firstNoteDescription.getText());


        }

        @Order(3)
        @Test
        @DisplayName("Test deleting previous note")
        public void testDeleteNote() {

//            delete note
            homePage = new HomePage(driver);
            homePage.deleteFirstNote();

//            check for success
            new WebDriverWait(driver, Duration.ofSeconds(1))
                    .until(ExpectedConditions.visibilityOfElementLocated(successDiv));
            List<WebElement> success = driver.findElements(successDiv);
            assertTrue(success.size() > 0);


//            return to home page and check for edited note
            resultPage = new ResultPage(driver);
            resultPage.returnToHomePage();

//            check note is no longer displayed
            homePage.goToNotesTab();
            new WebDriverWait(driver, Duration.ofSeconds(1))
                    .until(ExpectedConditions.elementToBeClickable(homePage.addNoteButton));

            assertEquals(0, homePage.notesList.size());
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
