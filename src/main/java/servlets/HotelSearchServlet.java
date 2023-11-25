package servlets;

import hotelData.Hotel;
import org.apache.commons.text.StringEscapeUtils;
import server.DatabaseHandler;
import thyemeleaf.ThymeLeafConfig;
import thyemeleaf.ThymeLeafRenderer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import reviewData.*;

public class HotelSearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String hotelName = request.getParameter("hotelName");
//        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
//        Hotel hotel = dbHandler.getHotelByName(hotelName);
//        List<Review> reviewList = dbHandler.getReviewsById(hotel.getHotelId());
//        DatabaseHandler handler = DatabaseHandler.getInstance();
//        double averageRating = handler.getAverageReviews(hotel.getHotelId());
//        System.out.println(hotel.toString());
//        System.out.println(reviewList.toString());
//        response.setContentType("text/html");
//        PrintWriter out = response.getWriter();
//        if (hotel != null) {
//            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
//            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
//            thymeleafRenderer.setVariable("hotel",hotel);
//            thymeleafRenderer.setVariable("reviews",reviewList);
//            thymeleafRenderer.setVariable("averageRating",averageRating);
//            thymeleafRenderer.render("hotel-details", out);
//        } else {
//            out.println("<p>Hotel not found</p>");
//        }
        PrintWriter out = response.getWriter();
        String keyword = request.getParameter("word");
        System.out.println(keyword);
        DatabaseHandler handler = DatabaseHandler.getInstance();
        List<Hotel> hotels = handler.getHotelNamesByKeyword(keyword);
        ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
        ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
        thymeleafRenderer.setVariable("hotels",hotels);
        thymeleafRenderer.render("hotelSearch", out);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
