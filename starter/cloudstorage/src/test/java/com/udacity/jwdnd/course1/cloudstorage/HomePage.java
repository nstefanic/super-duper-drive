package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage {

//    ************ NOTES Elements ************
    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(css = "#userTable tbody tr")
    public List<WebElement> notesList;

    @FindBy(id = "add-note-btn")
    public WebElement addNoteButton;

    @FindBy(id = "note-title")
    public WebElement noteTitleInput;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionInput;

    @FindBy(id = "note-save-changes-btn")
    private WebElement noteSaveChangesBtn;

    @FindBy(xpath = "//table[@id='userTable']//tbody/tr[1]/th[1]")
    public WebElement firstNoteTitle;

    @FindBy(xpath = "//table[@id='userTable']//tbody/tr[1]/td[2]")
    public WebElement firstNoteDescription;

    @FindBy(xpath = "//table[@id='userTable']//tbody/tr[1]/td[1]/a[1]")
    public WebElement firstNoteDeleteButton;

    @FindBy(xpath = "//table[@id='userTable']//tbody/tr[1]/td[1]/button[1]")
    public WebElement firstNoteEditButton;


//    ************ FILES Elements ************
    @FindBy(id = "nav-files-tab")
    private WebElement filesTab;

    @FindBy(xpath = "//input[@id='fileUpload']")
    public WebElement fileUploadInput;

    @FindBy(id = "uploadButton")
    public WebElement uploadButton;

    @FindBy(xpath = "//table[@id='fileTable']//tbody/tr[1]/td[1]/a[1]")
    public WebElement firstFileViewButton;

    @FindBy(xpath = "//table[@id='fileTable']//tbody/tr[1]/td[1]/a[2]")
    public WebElement firstFileDownloadButton;

    @FindBy(xpath = "//table[@id='fileTable']//tbody/tr[1]/td[1]/a[3]")
    public WebElement firstFileDeleteButton;

    @FindBy(xpath = "//table[@id='fileTable']//tbody/tr[1]/th[1]")
    public WebElement firstFileName;




//    ************ CREDENTIALS Elements ************
    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id="add-credential-btn")
    public WebElement addCredentialButton;
    @FindBy(id="credential-url")
    public WebElement credentialUrlInput;
    @FindBy(id="credential-username")
    private WebElement credentialUsernameInput;
    @FindBy(id="credential-password")
    private WebElement credentialPasswordInput;
    @FindBy(xpath = "//div[@id='credentialModal']//div[1]/div[1]/div[3]/button[2]")
    private WebElement credentialSaveChangesBtn;
    @FindBy(xpath = "//table[@id='credentialTable']//tbody/tr[1]/th[1]")
    public WebElement firstCredentialUrl;
    @FindBy(xpath = "//table[@id='credentialTable']//tbody/tr[1]/td[2]")
    public WebElement firstCredentialUsername;
    @FindBy(xpath = "//table[@id='credentialTable']//tbody/tr[1]/td[3]")
    public WebElement firstCredentialPassword;
    @FindBy(xpath = "//table[@id='credentialTable']//tbody/tr[1]/td[1]/a[1]")
    public WebElement firstCredentialDeleteButton;
    @FindBy(xpath = "//table[@id='credentialTable']//tbody/tr[1]/td[1]/button[1]")
    public WebElement firstCredentialEditButton;
    @FindBy(css = "#credentialTable tbody tr")
    public List<WebElement> credentialsList;


    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }




//    ************ NOTES Methods ************
    public void goToNotesTab() {
        notesTab.click();
    }
    public void clickAddNoteButton() {
        addNoteButton.click();
    }
    public void addNote(String title, String description) {
        noteTitleInput.sendKeys(title);
        noteDescriptionInput.sendKeys(description);
        noteSaveChangesBtn.click();
    }
    public void openFirstNoteToEdit() {
        System.out.println("firstNoteEditButton: " + firstNoteEditButton.getText());
        System.out.println("clicking on firstNoteEditButton");
        firstNoteEditButton.click();
    }
    public void editFirstNote(String title, String description) {
        noteTitleInput.clear();
        noteTitleInput.sendKeys(title);
        noteDescriptionInput.clear();
        noteDescriptionInput.sendKeys(description);
        noteSaveChangesBtn.click();
    }
    public void deleteFirstNote() {
        firstNoteDeleteButton.click();
    }





//    ************ FILES Methods ************
    public void goToFilesTab() {
        filesTab.click();
    }

    public void uploadFile(String filePath) {
        fileUploadInput.sendKeys(filePath);
        uploadButton.click();
    }
    public void viewFirstFile() {
        System.out.println("clicking on firstFileViewButton");
        firstFileViewButton.click();
    }

    public void downloadFirstFile() {
        System.out.println("clicking on firstFileDownloadButton");
        firstFileDownloadButton.click();
    }

    public void deleteFirstFile() {
        System.out.println("clicking on firstFileDeleteButton");
        firstFileDeleteButton.click();
    }




//    ************ CREDENTIALS Methods ************
    public void goToCredentialsTab() {
        credentialsTab.click();
    }

    public void clickAddCredentialButton() {
        addCredentialButton.click();
    }

    public void addCredential(String url, String username, String password) {
        credentialUrlInput.sendKeys(url);
        credentialUsernameInput.sendKeys(username);
        credentialPasswordInput.sendKeys(password);
        credentialSaveChangesBtn.click();
    }

    public void openFirstCredentialToEdit() {
        System.out.println("firstCredentialEditButton: " + firstCredentialEditButton.getText());
        System.out.println("clicking on firstCredentialEditButton");
        firstCredentialEditButton.click();
    }

    public void editFirstCredential(String url, String username, String password) {
        credentialUrlInput.clear();
        credentialUrlInput.sendKeys(url);
        credentialUsernameInput.clear();
        credentialUsernameInput.sendKeys(username);
        credentialPasswordInput.clear();
        credentialPasswordInput.sendKeys(password);
        credentialSaveChangesBtn.click();
    }

    public void deleteFirstCredential() {
        firstCredentialDeleteButton.click();
    }



}
