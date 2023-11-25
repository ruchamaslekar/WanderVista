package servlets;

import hotelData.*;
import hotelData.ThreadSafeHotelDetails;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import server.*;
import java.util.*;
import thyemeleaf.ThymeLeafConfig;
import thyemeleaf.ThymeLeafRenderer;

public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            response.sendRedirect("/login");
        } else {
            PrintWriter out = response.getWriter();
            System.out.println();
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.render("home", out);
        }
    }

}
