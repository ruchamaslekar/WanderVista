package servlets;

import hotelData.Hotel;
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
import java.sql.SQLException;
import java.util.List;

public class FavouriteHotelServlet extends HttpServlet {

    /**
     * Handles GET request to /favouriteHotel
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("username") == null) {
            resp.sendRedirect("/login");
        } else {
            String hotelName = req.getParameter("hotelName");
            System.out.println("hotelName" + hotelName);
            DatabaseHandler handler = DatabaseHandler.getInstance();
            Hotel hotel = handler.getHotelByName(hotelName);
            System.out.println("hotel" + hotel.toString());
            String username = (String) session.getAttribute("username");
            String userid = handler.getUserByName(username);
            List<String> hotelList = handler.getFavouriteHotel(userid,hotel.getHotelId());
            if (hotelList.isEmpty()) {
                try {
                    handler.insertIntoFavouriteHotelsTable(userid,hotel);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            resp.sendRedirect("/hotelDetails?hotelName=" + hotelName);
        }
    }

    /**
     * Handles POST request to /favouriteHotel
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            response.sendRedirect("/login");
        } else {
            String username = (String) session.getAttribute("username");
            PrintWriter out = response.getWriter();
            DatabaseHandler handler = DatabaseHandler.getInstance();
            String userid =  handler.getUserByName(username);
            handler.deleteFavoriteHotels(userid);
            List<List<String>> list = handler.getAllFavouriteHotels(userid);
            System.out.println(list.toString());
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.setVariable("list", list);
            thymeleafRenderer.render("favorite-hotels", out);
            out.flush();
        }
    }
}
