package reviewData;

import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Demonstrating ReviewParser class */
public class ReviewParser {

    List<Review> reviewList = new ArrayList<>();

    /**
     * This method is parsing multiple review.json file
     * that contains info about reviews using GSON library.
     * @param filePath path to the json file
     */
    public List<Review> parseReviewJson(File filePath) throws IOException {
        /** Creating object of Gson library */
        Gson gson = new Gson();
        /** Reading json files*/
        try (FileReader reader = new FileReader(filePath)) {
            JsonObject object = (JsonObject) JsonParser.parseReader(reader);
            JsonObject reviewDetailsObject = (JsonObject) object.get("reviewDetails");
            JsonObject reviewCollectionObject = (JsonObject) reviewDetailsObject.get("reviewCollection");
            JsonArray reviewArray = reviewCollectionObject.getAsJsonArray("review");
            /** Inserting values in rev iewMap */
            for (JsonElement element : reviewArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                String hotelId = jsonObject.get("hotelId").getAsString();
                String reviewId = jsonObject.get("reviewId").getAsString();
                double ratingOverall = jsonObject.get("ratingOverall").getAsDouble();
                String title = jsonObject.get("title").getAsString();
                String reviewText = jsonObject.get("reviewText").getAsString();
                String userNickname = jsonObject.get("userNickname").getAsString();
                String date = jsonObject.get("reviewSubmissionTime").getAsString();
                Review review = new Review(reviewId, ratingOverall, title, reviewText, userNickname, date,hotelId);
                reviewList.add(review);
            }

        } catch (IOException e) {
            System.out.println("File not found:" + e);
        }
    return  reviewList;
    }
}