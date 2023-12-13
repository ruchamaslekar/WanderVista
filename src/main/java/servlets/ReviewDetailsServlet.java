package servlets;

import com.google.gson.JsonObject;
import hotelData.Hotel;
import org.apache.commons.text.StringEscapeUtils;
import server.DatabaseHandler;
import server.JsonHelper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class ReviewDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            response.sendRedirect("/login");
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            String hotelName = request.getParameter("hotelName");
            Hotel hotel = DatabaseHandler.getInstance().getHotelByName(hotelName);
            String offset = request.getParameter("offset");
            String limit = request.getParameter("limit");
            String hotelId= hotel.getHotelId();
            if (hotelId != null && !hotelId.isEmpty())
                hotelId = StringEscapeUtils.escapeHtml4(hotelId);
            if (offset != null && !offset.isEmpty())
                offset = StringEscapeUtils.escapeHtml4(offset);
            if (limit != null && !limit.isEmpty())
                limit = StringEscapeUtils.escapeHtml4(limit);
            JsonHelper jsonResponseHelper = new JsonHelper();
            System.out.println("offset"+offset);
            JsonObject reviewJson = jsonResponseHelper.getAllReviewsInJsonFormat(hotel.getHotelId(), limit, offset);
            System.out.println(reviewJson.toString());
            PrintWriter out = response.getWriter();
            out.println(reviewJson);
            out.flush();
        }
    }
}
