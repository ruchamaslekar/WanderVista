package servlets;

import org.apache.commons.text.StringEscapeUtils;
import server.DatabaseHandler;
import thyemeleaf.ThymeLeafConfig;
import thyemeleaf.ThymeLeafRenderer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        if (request.getParameter("username") == null) {
            String username = request.getParameter("username");
            username = StringEscapeUtils.escapeHtml4(username);
            System.out.println();
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.render("login", out);
        }
        else  {
            response.sendRedirect("/home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        //check why flag is false
        boolean flag = dbHandler.authenticateUser(username, password);
        if (flag) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.getLastAccessedTime();
            response.sendRedirect("/login?username=" + username);
        }
        else {
            String error ="Error logging in ";
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.setVariable("error",error);
            thymeleafRenderer.render("login", out);
        }

        }
    }
