package com.github.alexeysa83.finalproject.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UserPageServlet", urlPatterns = {"/userpage"})
public class UserPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter wr = resp.getWriter();
        wr.write("<p><span style='color: blue;'>Hello, it is a restricted zone for authorized users!</span></p>");
    }
}
