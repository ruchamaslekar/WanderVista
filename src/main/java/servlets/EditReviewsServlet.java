package servlets;

import hotelData.Hotel;
import reviewData.Review;
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


public class EditReviewsServlet extends HttpServlet {

    /**
     * Handles GET request to /editReviews
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            response.sendRedirect("/login");
        }else {
            PrintWriter out = response.getWriter();
            String hotelId = request.getParameter("hotelId");
            String reviewId = request.getParameter("reviewId");
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            DatabaseHandler handler = DatabaseHandler.getInstance();
            Review review = handler.getReviewsByReviewIdAndHotelId(hotelId, reviewId);
            String hotelName = handler.getHotelById(hotelId).getHotelName();
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.setVariable("review", review);
            thymeleafRenderer.setVariable("hotelId", hotelId);
            thymeleafRenderer.setVariable("hotelName", hotelName);
            thymeleafRenderer.render("editReviews", out);
        }
    }

    /**
     * Handles POST request to /editReviews
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username =(String) session.getAttribute("username");
        String reviewId = request.getParameter("reviewId");
        String hotelId = request.getParameter("hotelId");
        String title;
        String reviewText;
        Double ratingOverall = Double.parseDouble(request.getParameter("ratingOverall"));
        if (request.getParameter("title") == null) {
            title = "";
        } else {
            title = request.getParameter("title");
        }
        if (request.getParameter("reviewText") == null) {
            reviewText = "";
        } else {
            reviewText = request.getParameter("reviewText");
        }
        Review review = new Review(reviewId,ratingOverall,title,reviewText,username, LocalDate.now().toString(),hotelId);
        DatabaseHandler handler = DatabaseHandler.getInstance();
        handler.updateReviewDetails(review);
        Hotel hotel = handler.getHotelById(hotelId);
        String hotelName = hotel.getHotelName();
        response.sendRedirect("/deleteReviews?hotelId="+hotelId+"&hotelName="+hotelName);
    }
}
