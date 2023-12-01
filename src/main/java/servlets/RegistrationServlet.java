package servlets;

import org.apache.commons.text.StringEscapeUtils;
import server.DatabaseHandler;
import thyemeleaf.ThymeLeafConfig;
import thyemeleaf.ThymeLeafRenderer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationServlet extends HttpServlet {

    /**
     * Handles GET request to /register
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
        ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
        thymeleafRenderer.render("register", out);
        out.flush();
    }

    /**
     * Handles POST request to /register
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        username = StringEscapeUtils.escapeHtml4(username);
        String password = request.getParameter("password");
        password = StringEscapeUtils.escapeHtml4(password);
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        boolean flag = dbHandler.getUser(username);
        if(flag){
            String message = "User with these credentials already exist.Please try again";
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.setVariable("message",message);
            thymeleafRenderer.render("login", out);
        } else {
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            String passError = "";
            String success = "";
            String regex = "(?=.{8,})(?=.*[A-Z])(?=.*\\d)(?=.*[$%@#])(.*)";
            Pattern p = Pattern.compile(regex);
            Matcher matcher = p.matcher(password);
            boolean checkPassword = matcher.matches();
            System.out.println(checkPassword);
            if (checkPassword) {
                dbHandler.registerUser(username, password);
                success = "Registration done successfully! Please login to continue";
                thymeleafRenderer.setVariable("success", success);
                thymeleafRenderer.render("login", out);
            } else {
                passError = "Password does not meet requirements.Please try again";
                thymeleafRenderer.setVariable("passError", passError);
                thymeleafRenderer.render("register", out);
            }

            out.flush();
        }

    }
}
