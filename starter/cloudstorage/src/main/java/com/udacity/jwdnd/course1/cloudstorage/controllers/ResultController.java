package com.udacity.jwdnd.course1.cloudstorage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("SameReturnValue")
@Controller
public class ResultController {

    @GetMapping("/result")
    public String getResultPage() {
        return "result";
    }
}
