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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@SuppressWarnings("SameReturnValue")
@Controller
public class CredentialsController {

    private final CredentialsService credentialsService;
    private final UserService userService;

    public CredentialsController(CredentialsService credentialsService, UserService userService) {

        this.credentialsService = credentialsService;
        this.userService = userService;
    }

    @PostMapping("/credentials")
    public String addCredentials(Authentication authentication, SavedCredential credential, RedirectAttributes redirectAttributes, Model model) {

        redirectAttributes.addFlashAttribute("activeTab", "credentials");

        User user = userService.getUser(authentication.getName());
        Integer userId = user.getUserId();
        credential.setUserId(userId);

//        check if username property is empty
        String username = credential.getUsername();
        if (username.isBlank() || username == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Username cannot be empty.");
            return "redirect:/result";
        }

//        check if password property is empty
        String password = credential.getPassword();
        if (password.isBlank() || password == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Password cannot be empty.");
            return "redirect:/result";
        }

//        check if url property is empty
        String url = credential.getUrl();
        if (url.isBlank() || url == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "URL cannot be empty.");
            return "redirect:/result";
        }

        int result = credentialsService.addOrUpdateCredential(credential);
        if (result <= 0) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "There was an error adding/updating the credential.");
        } else {
            redirectAttributes.addFlashAttribute("success", "true");
        }

        return "redirect:/result";
    }

    @GetMapping("/deleteCredential")
    public String deleteCredential(@RequestParam("id") Integer credentialId,  Authentication authentication, RedirectAttributes redirectAttributes, Model model) {
        System.out.println("attempting to delete credential with id: " + credentialId);

        redirectAttributes.addFlashAttribute("activeTab", "credentials");


        try {
            User user = userService.getUser(authentication.getName());
            Integer userId = user.getUserId();

            credentialsService.deleteCredential(credentialId, userId);
            redirectAttributes.addFlashAttribute("success", true);
            System.out.println("deleteCredential success");
            return "redirect:/result";
        } catch (Exception e) {
            System.out.println("deleteCredential error");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "There was an error deleting the credential.");
            return "redirect:/result";
        }

//        Success

    }
}
