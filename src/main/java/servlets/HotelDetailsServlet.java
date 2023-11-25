package servlets;

import hotelData.Hotel;
import reviewData.*;
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

public class HotelDetailsServlet extends HttpServlet {

    /**
     * Handles GET request to /hotelDetails
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String hotelName = request.getParameter("hotelName");
        DatabaseHandler handler = DatabaseHandler.getInstance();
        Hotel hotel = handler.getHotelByName(hotelName);
        List<Review> reviewList = handler.getReviewsById(hotel.getHotelId());
        double averageRating = handler.getAverageReviews(hotel.getHotelId());
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if (hotel != null) {
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.setVariable("hotel",hotel);
            System.out.println(hotel.getHotelId());
            thymeleafRenderer.setVariable("reviews",reviewList);
            thymeleafRenderer.setVariable("averageRating",averageRating);
            thymeleafRenderer.render("hotel-details", out);
        } else {
            out.println("<p>Hotel not found</p>");
        }
    }
}
