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

    public static void forwardToJspMessage
            (String jspName, String message, HttpServletRequest req, HttpServletResponse resp)  {
        req.setAttribute("message", message);
        try {
            req.getRequestDispatcher("/" + jspName + ".jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void forwardToServlet(String servletPath, HttpServletRequest req, HttpServletResponse resp)  {
        try {
            req.getRequestDispatcher(servletPath).forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void forwardToServletMessage
            (String servletPath, String message, HttpServletRequest req, HttpServletResponse resp)  {
        req.setAttribute("message", message);
        try {
            req.getRequestDispatcher(servletPath).forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void redirect(String page, HttpServletRequest rq, HttpServletResponse rs) {
        try {
            rs.sendRedirect(rq.getContextPath() + page);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
