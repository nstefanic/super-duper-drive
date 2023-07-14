package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {

    @FindBy(id = "success")
    public WebElement successHeading;
    @FindBy(id = "error-heading")
    public WebElement errorHeading;

    @FindBy(tagName = "a")
    private WebElement continueLink;

    public ResultPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void returnToHomePage() {
        continueLink.click();
    }
}
