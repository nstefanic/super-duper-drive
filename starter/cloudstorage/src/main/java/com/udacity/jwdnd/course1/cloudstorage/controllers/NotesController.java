package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@SuppressWarnings("SameReturnValue")
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
        model.addAttribute("activeTab", "notes");

        User user = userService.getUser(authentication.getName());
        Note[] notes = noteService.getAllNotes(user.getUserId());
        model.addAttribute("allNotes", notes);
        return "home";
    }

    @GetMapping("/notes/{noteId}")
    public String getNotes(@PathVariable Integer noteId, Authentication authentication, Model model) {
        model.addAttribute("activeTab", "notes");

        User user = userService.getUser(authentication.getName());

        if(user == null) {
            model.addAttribute("error", true);
            model.addAttribute("message", "User must be logged in.");
            return "result";
        }

        if(noteId == null) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Note not found.");
            return "result";
        }

        Note[] notes = noteService.getAllNotes(user.getUserId());
        model.addAttribute("allNotes", notes);
        return "home";
    }

    @PostMapping("/notes")
    public String postNotes(@ModelAttribute Note note, Authentication authentication, RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addFlashAttribute("activeTab", "notes");

        User user = userService.getUser(authentication.getName());

        if(user == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "User must be logged in.");
            return "redirect:/result";
        }

        note.setUserId(user.getUserId());

        String noteTitle = note.getNoteTitle();
        String noteDescription = note.getNoteDescription();

//        check if noteTitle and noteDescription are empty
        if(noteTitle.isBlank()  || noteTitle == null || noteDescription.isBlank() || noteDescription == null) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Note title and description cannot be empty.");
            return "redirect:/result";
        }


        int added = noteService.addOrEditNote(note);
        if (added < 0) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "There was an error adding the note. Please try again.");
            return "redirect:/result";
        }

//        redirectAttributes.addFlashAttribute("allNotes", noteService.getAllNotes(user.getUserId()));
        redirectAttributes.addFlashAttribute("success", true);
//        redirectAttributes.addFlashAttribute("activeTab", "notes");

        return "redirect:/result";
    }

    @GetMapping("/deleteNote")
    public String deleteNote(@RequestParam("id") Integer noteId, Authentication authentication, RedirectAttributes redirectAttributes, Model model) {
        System.out.println("attempting to delete noteId: " + noteId);

        redirectAttributes.addFlashAttribute("activeTab", "notes");

        try{
            User user = userService.getUser(authentication.getName());

            if(user == null) {
                redirectAttributes.addFlashAttribute("error", true);
                redirectAttributes.addFlashAttribute("message", "User must be logged in.");
                return "redirect:/result";
            }


            noteService.deleteNote(noteId, user.getUserId());
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "There was an error deleting the note. Please try again.");
            return "redirect:/result";
        }

        return "redirect:/result";
    }
}
