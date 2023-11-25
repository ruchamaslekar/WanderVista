package servlets;

import hotelData.Hotel;
import server.DatabaseHandler;
import thyemeleaf.ThymeLeafConfig;
import thyemeleaf.ThymeLeafRenderer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class HotelServlet extends HttpServlet {

    /**
     * Handles GET request to /hotels
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String keyword = request.getParameter("word");
        System.out.println(keyword);
        DatabaseHandler handler = DatabaseHandler.getInstance();
        List<Hotel> hotels = handler.getHotelNamesByKeyword(keyword);
        ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
        ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
        thymeleafRenderer.setVariable("hotels",hotels);
        thymeleafRenderer.render("hotel-list", out);
    }

}
