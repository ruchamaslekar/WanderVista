package hotelData;

import com.google.gson.*;
import org.apache.commons.text.StringEscapeUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/** Demonstrating HotelParser class */
public class HotelParser {
    List<Hotel> hotelList = new ArrayList<>();


    /**
     * This method is parsing a json file
     * that contains info about hotels; using GSON library.
     * @param filePath String
     *  @param hotelMap HotelDetails
     */
    public List<Hotel> parseHotelJson(String filePath , HotelDetails hotelMap) {
        /** Reading json file hotels1.json */
        try(FileReader reader = new FileReader(filePath)) {
            JsonParser parser = new JsonParser();
            JsonObject jo = (JsonObject) parser.parse(reader);
            JsonArray jsonArr = jo.getAsJsonArray("sr");
            /** Reading elements in jsonArr */
            for(JsonElement element : jsonArr) {
                JsonObject jsonObject = element.getAsJsonObject();
                String name = jsonObject.get("f").getAsString();
                String id = jsonObject.get("id").getAsString();
                String address = jsonObject.get("ad").getAsString();
                String city = jsonObject.get("ci").getAsString();
                String state = jsonObject.get("pr").getAsString();
                String country = jsonObject.get("c").getAsString();
                JsonObject latLong = jsonObject.get("ll").getAsJsonObject();
                String longitude = latLong.get("lng").getAsString();
                String latitude = latLong.get("lat").getAsString();
                /** Adding values of fields in Map */
                Hotel hotel = new Hotel(id,name,address,latitude,longitude,city,state,country);
                System.out.println(hotel);
                hotelList.add(hotel);
                hotelMap.addHotel(id,new Hotel(id,name,address,latitude,longitude,city,state,country) );
            }
        } catch (IOException e) {
            System.out.println("File not found" + e);
        }
        return hotelList;
    }
}
