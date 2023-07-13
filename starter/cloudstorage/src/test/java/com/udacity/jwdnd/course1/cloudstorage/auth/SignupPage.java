package com.udacity.jwdnd.course1.cloudstorage.auth;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(id="inputFirstName")
    WebElement firstName;

    @FindBy(id="inputLastName")
    WebElement lastName;

    @FindBy(id="inputUsername")
    WebElement username;

    @FindBy(id="inputPassword")
    WebElement password;

    @FindBy(id="buttonSignUp")
    WebElement submitButton;

    public SignupPage(WebDriver driver) {
         PageFactory.initElements(driver, this);
    }

    private void clearFields() {
        this.firstName.clear();
        this.lastName.clear();
        this.username.clear();
        this.password.clear();
    }

    public SignupPage typeFirstName(String firstName) {
        this.firstName.sendKeys(firstName);
        return this;
    }

    public SignupPage typeLastName(String lastName) {
        this.lastName.sendKeys(lastName);
        return this;
    }

    public SignupPage typeUsername(String username) {
        this.username.sendKeys(username);
        return this;
    }

    public SignupPage typePassword(String password) {
        this.password.sendKeys(password);
        return this;
    }

    public SignupPage clickSubmit() {
        this.submitButton.click();
        return this;
    }



    public void signup(String firstName, String lastName, String username, String password) {
        typeFirstName(firstName);
        typeLastName(lastName);
        typeUsername(username);
        typePassword(password);
        this.submitButton.click();
    }
}
