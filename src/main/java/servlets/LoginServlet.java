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
            String originalURL = (String) session.getAttribute("originalURL");
            response.sendRedirect(originalURL);
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
                session.setAttribute("username", username);
                if(flag) {
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
