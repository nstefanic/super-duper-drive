package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.SavedCredential;
import com.udacity.jwdnd.course1.cloudstorage.models.UserFile;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final NoteService noteService;
    private final UserService userService;
    private final FileService fileService;
    private final CredentialsService credentialsService;
    private final EncryptionService encryptionService;

    public HomeController(NoteService noteService1, UserService userService, FileService fileService, CredentialsService credentialsService, EncryptionService encryptionService) {

        this.noteService = noteService1;
        this.userService = userService;
        this.fileService = fileService;
        this.credentialsService = credentialsService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String getHomePage(Authentication authentication, Model model) {

    // get user
    User user = userService.getUser(authentication.getName());
    Integer userId = user.getUserId();

//    get user's files
    UserFile[] files = fileService.getAllFilesForUser(userId);
    model.addAttribute("allFiles", files);
//    model.addAttribute("file", new UserFile());

//    get user's notes
    Note[] notes = noteService.getAllNotes(userId);
    model.addAttribute("allNotes", notes);
    model.addAttribute("note", new Note());

//    get user's credentials
    SavedCredential[] credentials = credentialsService.getCredentials(user.getUserId());
    model.addAttribute("allCredentials", credentials);
    model.addAttribute("credential", new SavedCredential());

    model.addAttribute("encryptionService", encryptionService);

    return "home";

    }




}
