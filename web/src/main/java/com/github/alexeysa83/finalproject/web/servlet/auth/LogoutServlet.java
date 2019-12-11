package com.github.alexeysa83.finalproject.web.servlet.auth;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/auth/logout")
public class LogoutServlet {

    private static final Logger log = LoggerFactory.getLogger(LogoutServlet.class);

    @GetMapping
    public String doGet(HttpServletRequest req) {
        AuthUserDto authUser = (AuthUserDto) req.getSession().getAttribute("authUser");
        req.getSession().removeAttribute("authUser");
        req.getSession().invalidate();
        log.info("User id: {} logged out at: {}", authUser.getId(), LocalDateTime.now());
        return "login";
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
//        doGet(req, resp);
//    }
}
