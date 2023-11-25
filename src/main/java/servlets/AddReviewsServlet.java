package servlets;

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
import java.time.LocalDate;
import java.util.*;
import reviewData.*;
import hotelData.*;

public class AddReviewsServlet extends HttpServlet {

    /**
     * Handles GET request to /addReviews
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String hotelId = request.getParameter("hotelId");
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
        ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
        thymeleafRenderer.setVariable("hotelId",hotelId);
        thymeleafRenderer.render("addReviews", out);
    }

    /**
     * Handles POST request to /addReviews
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            response.sendRedirect("/login");
        } else {
            String reviewId = request.getParameter("reviewId");
            double rating;
            String title;
            String reviewText;
            String username;
            if (request.getParameter("rating").isEmpty()) {
                rating = 0.0;
            } else {
                rating = Double.parseDouble(request.getParameter("rating"));
            }
            if (request.getParameter("title").isEmpty()) {
                title = "";
            } else {
                title = request.getParameter("title");
            }
            if (request.getParameter("reviewText").isEmpty()) {
                reviewText = "";
            } else {
                reviewText = request.getParameter("reviewText");
            }
            username = (String) session.getAttribute("username");
            String hotelId = request.getParameter("hotelId");
            List<Review> reviews = new ArrayList<>();
            System.out.println(reviews);
            reviews.add(new Review(reviewId, rating, title, reviewText, username, LocalDate.now().toString(), hotelId));
            DatabaseHandler handler = DatabaseHandler.getInstance();
            Hotel hotel = handler.getHotelById(hotelId);
            String hotelName = hotel.getHotelName();
            handler.insertIntoReviews(reviews);
            response.sendRedirect("/hotelDetails?hotelName=" + hotelName);
        }
    }
}
