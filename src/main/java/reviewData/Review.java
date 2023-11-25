//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package reviewData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Review {
    private final String hotelId;
    private final String reviewId;
    private final double ratingOverall;
    private final String title;
    private final String reviewText;
    private final String userNickname;
    private final String date;

    public String getReviewText() {
        return this.reviewText;
    }

    public String getHotelId() {
        return this.hotelId;
    }

    public String getReviewId() {
        return this.reviewId;
    }

    public String getDate() {
        return this.date;
    }

    public double getRatingOverall() {
        return this.ratingOverall;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUserNickname() {
        return this.userNickname;
    }

    public Review(String reviewId, double ratingOverall, String title, String reviewText, String userNickname, String date,String hotelId) {
        this.hotelId = hotelId;
        this.reviewId = reviewId;
        this.ratingOverall = ratingOverall;
        this.title = title;
        this.reviewText = reviewText;
        this.userNickname = userNickname;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
//        this.date = LocalDate.parse(date, formatter);
        this.date = date;
    }

    public String toString() {
        String var10000 = this.userNickname;
        return "Review by " + var10000 + " on " + this.date + System.lineSeparator() + "Rating: " + this.ratingOverall + System.lineSeparator() + "ReviewId: " + this.reviewId + System.lineSeparator() + this.title + System.lineSeparator() + this.reviewText + System.lineSeparator();
    }

//    public String toStringDisplay() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String formattedDate = this.date.atStartOfDay().format(formatter);
//        String var10000 = this.reviewId;
//        return "ReviewId = " + var10000 + System.lineSeparator() + "averageRating = " + this.ratingOverall + System.lineSeparator() + "Title = " + this.title + System.lineSeparator() + "reviewText = " + this.reviewText + System.lineSeparator() + "userNickname = " + this.userNickname + System.lineSeparator() + "submissionDate = " + formattedDate;
//    }
}
