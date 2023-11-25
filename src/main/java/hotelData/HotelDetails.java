package hotelData;

import com.google.gson.JsonObject;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/** Demonstrating HotelDetails class */
public class HotelDetails {

    /** Initializing hotelMap */
    private final Map<String, Hotel> hotelMap = new TreeMap<>();

    /** This method is adding data into hotelMap
     * @param hotelId String
     * @param hotel Hotel
     * */
    public void addHotel(String hotelId, Hotel hotel) {
        hotelMap.put(hotelId, hotel);
    }

    /**
     * This method is printing Hotel data based on hotelID
     * @param hotelID String
     */
    public Hotel getHotel(String hotelID) {
        return hotelMap.get(hotelID);
    }


    /** This method is returning HotelIds of all hotels */
    public Set<String> getIdForHotel() {
        return Collections.unmodifiableSet(hotelMap.keySet());
    }

    /** A method to return hotel in JsonObject format
     * @param keyWord String
     */
    public JsonObject getHotelInJSONFormat(String keyWord) {
        JsonObject responseJson = new JsonObject();
            Hotel hotel = this.getHotel(keyWord);
            if (hotel != null) {
                responseJson.addProperty("hotelId", keyWord);
                responseJson.addProperty("name", hotel.getHotelName());
                responseJson.addProperty("addr", hotel.getAddress());
                responseJson.addProperty("city", hotel.getCity());
                responseJson.addProperty("state", hotel.getState());
                responseJson.addProperty("lat", hotel.getLatitude());
                responseJson.addProperty("lng", hotel.getLongitude());
                responseJson.addProperty("success", true);
            } else {
                responseJson.addProperty("hotelId", "invalid");
                responseJson.addProperty("success", false);
            }
        return responseJson;
    }
    /**
     * Method to return weather in JsonObject format
     * @param hotelId String
     */
//    public JsonObject getWeatherDataInJSONFormat(String hotelId) {
//        JsonObject responseJson = new JsonObject();
//        Hotel hotel = this.getHotel(hotelId);
//        if(hotel != null) {
//            String latitude = hotel.getLatitude();
//            String longitude = hotel.getLongitude();
//            JsonObject weatherDetails = WeatherFetcher.fetch("api.open-meteo.com","/v1/forecast?latitude="+latitude+"&longitude="+longitude+"&current_weather=true");
//            System.out.println(weatherDetails.toString());
//            responseJson.addProperty("hotelId", hotelId);
//            responseJson.addProperty("name", hotel.getHotelName());
//            responseJson.addProperty("addr", hotel.getAddress());
//            responseJson.addProperty("city", hotel.getCity());
//            responseJson.addProperty("state", hotel.getState());
//            responseJson.addProperty("lat", hotel.getLatitude());
//            responseJson.addProperty("lng", hotel.getLongitude());
//            responseJson.add("weather", weatherDetails);
//            responseJson.addProperty("success", true);
//        } else {
//            responseJson.addProperty("hotelId", "invalid");
//            responseJson.addProperty("success", false);
//        }
//        return responseJson;
//    }
}