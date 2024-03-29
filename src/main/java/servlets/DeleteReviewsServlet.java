package servlets;

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
import java.util.List;

public class DeleteReviewsServlet extends HttpServlet {
    /**
     * Handles GET request to /deleteReviews
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
            String hotelName = request.getParameter("hotelName");
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            String username = (String) session.getAttribute("username");
            DatabaseHandler handler = DatabaseHandler.getInstance();
            List<Review> reviewList = handler.getReviewsForUser(hotelId, username);
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.setVariable("reviews", reviewList);
            thymeleafRenderer.setVariable("hotelId", hotelId);
            thymeleafRenderer.setVariable("hotelName", hotelName);
            thymeleafRenderer.render("deleteReviews", out);
        }
    }

    /**
     * Handles POST request to /deleteReviews
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reviewId = request.getParameter("reviewId");
        String hotelId = request.getParameter("hotelId");

        String action = request.getParameter("action");
        if (action.equals("delete")) {
            DatabaseHandler handler = DatabaseHandler.getInstance();
            handler.deleteReview(reviewId,hotelId);
            String hotelName = handler.getHotelById(hotelId).getHotelName();
            response.sendRedirect("/deleteReviews?hotelId="+hotelId+"&hotelName="+hotelName);
        } else if (action.equals("edit")) {
            response.sendRedirect("/editReviews?hotelId="+hotelId+"&reviewId="+reviewId);
        }

    }
}
