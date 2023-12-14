package servlets;

import com.google.gson.Gson;
import hotelData.Hotel;
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

public class FetchHotelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        PrintWriter out = response.getWriter();
        List<Hotel> hotels = handler.getAllFavouriteHotels();
        if (request.getHeader("Accept").contains("application/json")) {
            Gson gson = new Gson();
            String jsonData = gson.toJson(hotels);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.write(jsonData);
            out.close();
        }
         else {
            ThymeLeafConfig thymeleafConfig = new ThymeLeafConfig();
            ThymeLeafRenderer thymeleafRenderer = new ThymeLeafRenderer(thymeleafConfig.templateEngine());
            thymeleafRenderer.setVariable("hotels", hotels);
            thymeleafRenderer.render("favorite-hotels", out);
        }
    }
}
