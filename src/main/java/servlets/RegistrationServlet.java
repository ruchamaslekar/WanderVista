package servlets;

import org.apache.commons.text.StringEscapeUtils;
import server.DatabaseHandler;
import thyemeleaf.ThymeLeafConfig;
import thyemeleaf.ThymeLeafRenderer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println();
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
        ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
        thymeleafRenderer.render("register", out);
        out.flush();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        String usernameParam = request.getParameter("username");
        usernameParam = StringEscapeUtils.escapeHtml4(usernameParam);
        String password = request.getParameter("password");
        password = StringEscapeUtils.escapeHtml4(password);

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.registerUser(usernameParam, password);

        ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
        ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
        thymeleafRenderer.render("success",out);

//        response.getWriter().println("Successfully registered the user " + usernameParam);
    }
}
