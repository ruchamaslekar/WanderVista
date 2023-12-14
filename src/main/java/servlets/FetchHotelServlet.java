package servlets;

import com.google.gson.Gson;
import hotelData.Hotel;
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

public class FetchHotelServlet extends HttpServlet {

    /**
     * Handles GET request to /fetchHotels
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            response.sendRedirect("/login");
        } else {
            DatabaseHandler handler = DatabaseHandler.getInstance();
            String userName = (String) session.getAttribute("username");
            String userId = handler.getUserByName(userName);
            PrintWriter out = response.getWriter();
            List<List<String>> hotels = handler.getAllFavouriteHotels(userId);
            System.out.println(hotels.toString());
            response.setContentType("text/html");
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.render("favorite-hotels", out);
            }
    }
}
