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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.servlet.http.HttpSession;

/** Servlet handles /login requests */
public class LoginServlet extends HttpServlet {


    /**
     * Handles GET request to /login
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        if (session.getAttribute("username") == null) {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.render("login", out);
        } else {
            response.sendRedirect("/home");
        }
    }

    /**
     * Handles POST request to /login
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        if(username != null && !username.equals("")) {
            if (password != null && !password.equals("")) {
                username = StringEscapeUtils.escapeHtml4(username);
                password = StringEscapeUtils.escapeHtml4(password);
                PrintWriter out = response.getWriter();
                DatabaseHandler dbHandler = DatabaseHandler.getInstance();
                boolean flag = dbHandler.authenticateUser(username, password);
                if(flag) {
                    session.setAttribute("username", username);
                    String userid = dbHandler.getUserByName(username);
                    String lastLogin = dbHandler.getLastLoginDetails(userid);
                    if(lastLogin.equals("N/A")){
                        session.setAttribute("lastLoginMessage", "You have not logged in before");
                        dbHandler.insertLastLoginDetails(userid, String.valueOf(LocalDateTime.now()));
                    } else{
                        LocalDateTime lastLoginDateTime = LocalDateTime.parse(lastLogin);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma, MM:dd:yyyy");
                        String formattedLastLogin = lastLoginDateTime.format(formatter);
                        session.setAttribute("lastLoginMessage", "Last login: " + formattedLastLogin);
                        dbHandler.updateLastLoginDetails(userid,String.valueOf(LocalDateTime.now()));
                    }
                    response.sendRedirect("/home");
                } else {
                    String error = "Error logging in ";
                    ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
                    ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
                    thymeleafRenderer.setVariable("error", error);
                    thymeleafRenderer.render("login", out);
                }
            }
        }
    }
}
