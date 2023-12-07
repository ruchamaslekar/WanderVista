package servlets;

import hotelData.Hotel;
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
import java.util.List;

public class ExpediaHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            response.sendRedirect("/login");
        } else {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            String username = (String) session.getAttribute("username");
            System.out.println(username);
            PrintWriter out = response.getWriter();
            DatabaseHandler handler = DatabaseHandler.getInstance();
            String userid = handler.getUserByName(username);
            System.out.println(userid);
            List<List<String>> list = handler.getExpediaVisitHistory(userid);
            System.out.println(list.toString());
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.setVariable("list", list);
            thymeleafRenderer.render("expediaHistory", out);
            out.flush();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            response.sendRedirect("/login");
        } else {
            String username = (String) session.getAttribute("username");
            PrintWriter out = response.getWriter();
            DatabaseHandler handler = DatabaseHandler.getInstance();
            String userid =  handler.getUserByName(username);
            handler.deleteExpediaHistory(userid);
            List<List<String>> list = handler.getExpediaVisitHistory(userid);
            System.out.println(list.toString());
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.setVariable("list", list);
            thymeleafRenderer.render("expediaHistory", out);
            out.flush();
        }
    }
}
