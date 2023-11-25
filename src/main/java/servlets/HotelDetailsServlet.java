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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String hotelName = request.getParameter("hotelName");
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        Hotel hotel = dbHandler.getHotelByName(hotelName);
        List<Review> reviewList = dbHandler.getReviewsById(hotel.getHotelId());
        DatabaseHandler handler = DatabaseHandler.getInstance();
        double averageRating = handler.getAverageReviews(hotel.getHotelId());
        System.out.println(hotel.toString());
        System.out.println(reviewList.toString());
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if (hotel != null) {
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.setVariable("hotel",hotel);
            thymeleafRenderer.setVariable("reviews",reviewList);
            thymeleafRenderer.setVariable("averageRating",averageRating);
            thymeleafRenderer.render("hotel-details", out);
        } else {
            out.println("<p>Hotel not found</p>");
        }

    }

}
