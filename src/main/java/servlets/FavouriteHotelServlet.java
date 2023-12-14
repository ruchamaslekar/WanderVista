package servlets;

import hotelData.Hotel;
import server.DatabaseHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FavouriteHotelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String hotelName = req.getParameter("hotelName");
        System.out.println("hotelName" + hotelName);
        DatabaseHandler handler = DatabaseHandler.getInstance();
        Hotel hotel = handler.getHotelByName(hotelName);
        System.out.println("hotel"+hotel.toString());
        List<Hotel> hotelList = handler.getFavouriteHotel(hotel.getHotelId());
        if( hotelList.isEmpty())
        {
            hotelList.add(hotel);
            try {
                handler.insertIntoFavouriteHotelsTable(hotel);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            for (Hotel h : hotelList) {
                if (!h.getHotelId().equals(hotel.getHotelId()))
                    try {
                        handler.insertIntoFavouriteHotelsTable(hotel);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
            }
        }

        resp.sendRedirect("/hotelDetails?hotelName=" + hotelName);
    }
}
