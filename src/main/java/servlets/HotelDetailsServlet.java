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
import java.text.DecimalFormat;
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
            PrintWriter out = response.getWriter();
            DatabaseHandler handler = DatabaseHandler.getInstance();
            Hotel hotel = handler.getHotelByName(hotelName);
            String hotelId = hotel.getHotelId();
            String username = (String) session.getAttribute("username");
            String city = hotel.getCity();
            List<Review> reviewList = handler.getReviewsById(hotel.getHotelId());
            double averageRating = handler.getAverageReviews(hotel.getHotelId());
            DecimalFormat df = new DecimalFormat("#.##");
            String formattedAverageRating = df.format(averageRating);
            String expediaLink = "https://www.expedia.com/" + city + "-Hotels-" + hotelName + ".h" + hotelId + ".Hotel-Information";
            response.setContentType("text/html");
            String userid = handler.getUserByName((String) session.getAttribute("username"));
            int visitCount = handler.getExpediaPageVisitCount(userid,hotelId);
            if (visitCount == 0) {
                handler.insertExpediaHistory(userid, hotelId, hotel.getHotelName(), expediaLink);
            } else{
                handler.updateExpediaHistory(userid,hotelId);
            }
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.setVariable("hotel", hotel);
            thymeleafRenderer.setVariable("username", username);
            thymeleafRenderer.setVariable("reviews", reviewList);
            thymeleafRenderer.setVariable("averageRating", formattedAverageRating);
            thymeleafRenderer.setVariable("expediaLink", expediaLink);
            thymeleafRenderer.render("hotel-details", out);
        }
    }
}
