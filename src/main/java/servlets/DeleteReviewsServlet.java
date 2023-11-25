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
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class DeleteReviewsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String hotelId = request.getParameter("hotelId");
        System.out.println(hotelId);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();
        String username = (String)session.getAttribute("username");
        System.out.println(username);
        DatabaseHandler handler = DatabaseHandler.getInstance();
        List<Review> reviewList = handler.getReviewsForUser(hotelId,username);
        ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
        ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
        thymeleafRenderer.setVariable("reviews",reviewList);
        thymeleafRenderer.setVariable("hotelId",hotelId);
        thymeleafRenderer.render("deleteReviews", out);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reviewId = req.getParameter("reviewId");
        String hotelId = req.getParameter("hotelId");
        System.out.println("reviewId "+ reviewId);
        System.out.println("hotelId "+ hotelId);
        DatabaseHandler handler = DatabaseHandler.getInstance();
        handler.deleteReview(reviewId,hotelId);
        resp.sendRedirect("/deleteReviews?hotelId="+hotelId);

    }
}
