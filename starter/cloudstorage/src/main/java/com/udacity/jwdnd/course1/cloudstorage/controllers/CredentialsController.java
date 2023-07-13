package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.SavedCredential;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CredentialsController {

    private final EncryptionService encryptionService;
    private final CredentialsService credentialsService;
    private final UserService userService;

    public CredentialsController(EncryptionService encryptionService, CredentialsService credentialsService, UserService userService) {
        this.encryptionService = encryptionService;

        this.credentialsService = credentialsService;
        this.userService = userService;
    }

    @PostMapping("/credentials")
    public String addCredentials(Authentication authentication, SavedCredential credential, Model model) {

        User user = userService.getUser(authentication.getName());
        Integer userId = user.getUserId();
        credential.setUserId(userId);

        int result = credentialsService.addOrUpdateCredential(credential);
        if (result < 0) {
            model.addAttribute("error", true);
            model.addAttribute("message", "There was an error adding/updating the credential.");
        } else {
            model.addAttribute("success", "true");
        }

        return "result";
    }

    @GetMapping("/deleteCredential")
    public String deleteCredential(@RequestParam("id") Integer credentialId,  Authentication authentication, Model model) {
        System.out.println("attempting to delete credential with id: " + credentialId);
        try {
            User user = userService.getUser(authentication.getName());
            Integer userId = user.getUserId();

            credentialsService.deleteCredential(credentialId, userId);
            model.addAttribute("success", true);
            System.out.println("deleteCredential success");
            return "result";
        } catch (Exception e) {
            System.out.println("deleteCredential error");
            e.printStackTrace();
            model.addAttribute("error", true);
            model.addAttribute("message", "There was an error deleting the credential.");
            return "result";
        }

//        Success

    }
}
