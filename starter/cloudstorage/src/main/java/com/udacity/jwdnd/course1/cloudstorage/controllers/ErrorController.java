package com.udacity.jwdnd.course1.cloudstorage.controllers;

import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("SameReturnValue")
public class ErrorController {

    @GetMapping("/error")
    public String getErrorPage() {
        return "error";
    }

}
