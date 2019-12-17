package com.github.alexeysa83.finalproject.web.controller.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping (value = "/errors/access_denied")
    public String forwardToAccessDeniedJSP(){
               return "access_denied";
    }
}
