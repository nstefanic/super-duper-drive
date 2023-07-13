//package com.udacity.jwdnd.course1.cloudstorage.auth;
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.FindBy;
//import org.openqa.selenium.support.PageFactory;
//
//import java.util.List;
//
//
//public class _DELETE_ChatPage {
//
//    @FindBy(id="messageText")
//    WebElement messageText;
//
//    @FindBy(id="submit-button")
//    WebElement submitButton;
//
//    @FindBy(className = "chatMessageText")
//    private List<WebElement> messageTexts;
//
//    @FindBy(className = "chatMessageUsername")
//    private List<WebElement> messageUsernames;
//
//    public _DELETE_ChatPage(WebDriver driver) {
//        PageFactory.initElements(driver, this);
//    }
//
//    public _DELETE_ChatPage typeMessage(String message) {
//        System.out.println("typing messageText: " + message);
//        this.messageText.sendKeys(message);
//        return this;
//    }
//
//    public _DELETE_ChatPage clickSubmit() {
//        System.out.println("clicking submitButton: ");
//        this.submitButton.click();
//        return this;
//    }
//
//    public _DELETE_ChatPage sendChatMessage(String message) {
//        typeMessage(message);
//        clickSubmit();
//        return this;
//    }
//
//    public ChatMessage getFirstMessage() {
//        ChatMessage message = new ChatMessage();
//        message.setUsername(messageUsernames.get(0).getText());
//        message.setMessageText(messageTexts.get(0).getText());
///*
//        ChatMessage message = new ChatMessage(
//            messageUsernames.get(0).getText(),
//                messageTexts.get(0).getText()
//        );
//*/
//
//        return message;
//    }
//
//}
