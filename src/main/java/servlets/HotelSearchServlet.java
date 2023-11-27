package servlets;

import hotelData.Hotel;
import server.DatabaseHandler;
import thyemeleaf.ThymeLeafConfig;
import thyemeleaf.ThymeLeafRenderer;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class HotelSearchServlet extends HttpServlet {
    /**
     * Handles GET request to /hotelSearch
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String originalURL = request.getRequestURI();
        session.setAttribute("originalURL", originalURL);
        if (session.getAttribute("username") == null) {
            response.sendRedirect("/login");
        }else {
            PrintWriter out = response.getWriter();
            String keyword = request.getParameter("word");
            System.out.println(keyword);
            DatabaseHandler handler = DatabaseHandler.getInstance();
            List<Hotel> hotels = handler.getHotelNamesByKeyword(keyword);
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.setVariable("hotels", hotels);
            thymeleafRenderer.render("hotelSearch", out);
        }

    }

    /**
     * Handles POST request to /hotelSearch
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            response.sendRedirect("/login");
        }else {
            PrintWriter out = response.getWriter();
            String keyword = request.getParameter("word");
            System.out.println(keyword);
            DatabaseHandler handler = DatabaseHandler.getInstance();
            List<Hotel> hotels = handler.getHotelNamesByKeyword(keyword);
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.setVariable("hotels", hotels);
            thymeleafRenderer.render("hotel-list", out);
        }
    }
}
