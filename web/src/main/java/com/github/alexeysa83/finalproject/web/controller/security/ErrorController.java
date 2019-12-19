package com.github.alexeysa83.finalproject.web.controller.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController {

    @RequestMapping(value = "/errors/access_denied", method = {RequestMethod.GET, RequestMethod.POST})
    public String forwardToAccessDeniedJSP(){
               return "access_denied";
    }
}
