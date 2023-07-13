package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NotesController {

    private final NoteService noteService;
    private final UserService userService;

    public NotesController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping("/notes")
    public String getNotes(Authentication authentication, Model model) {
        User user = userService.getUser(authentication.getName());
        Note[] notes = noteService.getAllNotes(user.getUserId());
        model.addAttribute("allNotes", notes);
        return "home";
    }

    @GetMapping("/notes/{noteId}")
    public String getNotes(@PathVariable Integer noteId, Authentication authentication, Model model) {
        User user = userService.getUser(authentication.getName());
        Note[] notes = noteService.getAllNotes(user.getUserId());
        model.addAttribute("allNotes", notes);
        return "home";
    }

    @PostMapping("/notes")
    public String postNotes(@ModelAttribute Note note, Authentication authentication, Model model) {
        User user = userService.getUser(authentication.getName());
        note.setUserId(user.getUserId());
        int added = noteService.addOrEditNote(note);
        if (added < 0) {
            model.addAttribute("error", true);
            model.addAttribute("message", "There was an error adding the note. Please try again.");
            return "result";
        }

//        model.addAttribute("allNotes", noteService.getAllNotes(user.getUserId()));
        model.addAttribute("success", true);
//        model.addAttribute("activeTab", "notes");

        return "result";
    }

    @GetMapping("/deleteNote")
    public String deleteNote(@RequestParam("id") Integer noteId, Authentication authentication, Model model) {
        System.out.println("attempting to delete noteId: " + noteId);
        try{
            User user = userService.getUser(authentication.getName());
            noteService.deleteNote(noteId, user.getUserId());
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "There was an error deleting the note. Please try again.");
            return "result";
        }

        return "result";
    }
}
