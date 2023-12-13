package server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import reviewData.*;

import java.util.Set;
import java.util.SortedSet;

public class JsonHelper {
    /**
     * Returns review details in Json format
     *
     * @param hotelId String
     * @param limit   String
     * @param offset  String
     * @return JsonObject
     */
    public JsonObject getAllReviewsInJsonFormat(String hotelId, String limit, String offset) {
        JsonObject reviewInfoResponse = new JsonObject();
        Set<Review> reviewSet = DatabaseHandler.getInstance().getLimitedReviews(hotelId, limit, offset);
        if (reviewSet != null) {
            if (reviewSet.size() > 0) {
                reviewInfoResponse.addProperty("success", true);
                reviewInfoResponse.addProperty("hotelId", hotelId);
                JsonArray reviewArray = new JsonArray();
                for (Review review : reviewSet) {
                    JsonObject reviewJson = new JsonObject();
                    reviewJson.addProperty("reviewId", review.getReviewId());
                    reviewJson.addProperty("title", review.getTitle());
                    reviewJson.addProperty("user", review.getUserNickname());
                    reviewJson.addProperty("reviewText", review.getReviewText());
                    reviewJson.addProperty("overallRating", review.getRatingOverall());
                    reviewJson.addProperty("date", review.getDate());
                    reviewArray.add(reviewJson);
                }
                reviewInfoResponse.add("reviews", reviewArray);
            } else {
                reviewInfoResponse.addProperty("success", false);
                reviewInfoResponse.addProperty("hotelId", "invalid");
            }
        } else {
            reviewInfoResponse.addProperty("success", false);
            reviewInfoResponse.addProperty("hotelId", "invalid");
        }
        return reviewInfoResponse;
    }
}
