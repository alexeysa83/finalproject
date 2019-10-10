package com.github.alexeysa83.finalproject.web.servlet.user;

import com.github.alexeysa83.finalproject.model.User;
import com.github.alexeysa83.finalproject.service.UtilsService;
import com.github.alexeysa83.finalproject.service.user.DefaultUserService;
import com.github.alexeysa83.finalproject.service.user.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.alexeysa83.finalproject.web.WebUtils.*;

@WebServlet(name = "UpdateUserServlet", urlPatterns = {"/restricted/user/update"})
public class UpdateUserServlet extends HttpServlet {

    private UserService userService = DefaultUserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String authId = req.getParameter("authId");
        final User user = userService.getById(authId);
        req.setAttribute("user", user);
        forwardToJsp("userupdate", req, resp);
    }

    // Validation + add null to DB instead of ""
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String authId = req.getParameter("authId");
        final long id = UtilsService.stringToLong(authId);
        final String firstName = req.getParameter("firstName");
        final String lastName = req.getParameter("lastName");
        final String email = req.getParameter("email");
        final String phone = req.getParameter("phone");
        final boolean isUpdated = userService.update(new User(firstName, lastName, email, phone, id));
        String message = "Update succesfull";
        if (!isUpdated) {
            message = "Update cancelled, please try again";
        }
        forwardToServletMessage("/restricted/user/profile", message, req, resp);
    }
}
