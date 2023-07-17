package com.udacity.jwdnd.course1.cloudstorage.auth;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id="inputUsername")
    WebElement username;

    @FindBy(id="inputPassword")
    WebElement password;

    @FindBy(id="login-button")
    WebElement submitButton;

    @FindBy(id="logged-out")
    public WebElement loggedOutMessage;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public LoginPage typeUsername(String username) {
        System.out.println("Typing username: " + username);
        this.username.sendKeys(username);
        return this;
    }

    public LoginPage typePassword(String password) {
        System.out.println("Typing password: " + password);
        this.password.sendKeys(password);
        return this;
    }

    public LoginPage clickSubmit() {
        System.out.println("Clicking submit.");
        this.submitButton.click();
        return this;
    }

    public void login(String username, String password) {
        typeUsername(username);
        typePassword(password);
        clickSubmit();
    }



}
