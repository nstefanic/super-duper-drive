package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationService authService;
    private final UserService userService;

    public LoginController(AuthenticationService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }
    @GetMapping
    public String getLoginPage() {
        return "login";
    }

//    @PostMapping
//    public String postLoginPage(Authentication authentication, User user, Model model) {
//        if(user.getUsername() != null) {
//            authService.authenticate(authentication)
//                    .getAuthorities()
//                    .stream()
//                    .forEach(a -> System.out.println(a.getAuthority()));
////                return "redirect:/home";
//        }
//
//        if( authentication != null ) {
//            return "redirect:/home";
//        } else {
//            model.addAttribute("loginError", true);
//        }
//
//
//        return "login";
//    }
}
