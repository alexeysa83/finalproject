package com.github.alexeysa83.finalproject.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class WebUtils {

    private WebUtils() {
    }

    public static void forwardToJsp (String jspName, HttpServletRequest req, HttpServletResponse resp)  {
        try {
            req.getRequestDispatcher("/" + jspName + ".jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
