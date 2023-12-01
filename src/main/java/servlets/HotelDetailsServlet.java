package servlets;

import hotelData.Hotel;
import reviewData.*;
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

public class HotelDetailsServlet extends HttpServlet {

    /**
     * Handles GET request to /hotelDetails
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            response.sendRedirect("/login");
        }else {
            String hotelName = request.getParameter("hotelName");
            DatabaseHandler handler = DatabaseHandler.getInstance();
            Hotel hotel = handler.getHotelByName(hotelName);
            String hotelId = hotel.getHotelId();
            String city = hotel.getCity();
            List<Review> reviewList = handler.getReviewsById(hotel.getHotelId());
            double averageRating = handler.getAverageReviews(hotel.getHotelId());
            String expediaLink = "https://www.expedia.com/" + city + "-Hotels-" + hotelName + ".h" + hotelId + ".Hotel-Information";
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            if (hotel != null) {
                ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
                ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
                thymeleafRenderer.setVariable("hotel", hotel);
                thymeleafRenderer.setVariable("reviews", reviewList);
                thymeleafRenderer.setVariable("averageRating", averageRating);
                thymeleafRenderer.setVariable("expediaLink", expediaLink);
                thymeleafRenderer.render("hotel-details", out);
            } else {
                out.println("<p>Hotel not found</p>");
            }
        }
    }
}
