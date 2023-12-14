package server;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import hotelData.*;
import java.util.*;
import  reviewData.*;


/**
 *
 * Modified from the example of Prof. Engle
 */
public class DatabaseHandler {

    private static DatabaseHandler dbHandler = new DatabaseHandler(".idea/database.properties"); // singleton pattern
    private Properties config; // a "map" of properties
    private String uri = null; // uri to connect to mysql using jdbc
    private Random random = new Random(); // used in password  generation

    /**
     * DataBaseHandler is a singleton, we want to prevent other classes
     * from creating objects of this class using the constructor
     */
    private DatabaseHandler(String propertiesFile) {
        this.config = loadConfigFile(propertiesFile);
        this.uri = "jdbc:mysql://" + config.getProperty("hostname") + "/" + config.getProperty("database") + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    }

    /**
     * Returns the instance of the database handler.
     *
     * @return instance of the database handler
     */
    public static DatabaseHandler getInstance() {
        return dbHandler;
    }


    // Load info from config file database.properties
    public Properties loadConfigFile(String propertyFile) {
        Properties config = new Properties();
        try (FileReader fr = new FileReader(propertyFile)) {
            config.load(fr);
        } catch (IOException e) {
            System.out.println(e);
        }

        return config;
    }

    public void createTable() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            statement = dbConnection.createStatement();
            statement.executeUpdate(PreparedStatements.CREATE_USER_TABLE);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }


    /**
     * Returns the hex encoding of a byte array.
     *
     * @param bytes  - byte array to encode
     * @param length - desired length of encoding
     * @return hex encoded byte array
     */
    public static String encodeHex(byte[] bytes, int length) {
        BigInteger bigint = new BigInteger(1, bytes);
        String hex = String.format("%0" + length + "X", bigint);
        assert hex.length() == length;
        return hex;
    }

    /**
     * Calculates the hash of a password and salt using SHA-256.
     *
     * @param password - password to hash
     * @param salt     - salt associated with user
     * @return hashed password
     */
    public static String getHash(String password, String salt) {
        String salted = salt + password;
        String hashed = salted;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salted.getBytes());
            hashed = encodeHex(md.digest(), 64);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return hashed;
    }

    /**
     * Registers a new user, placing the username, password hash, and
     * salt into the database.
     *
     * @param newuser - username of new user
     * @param newpass - password of new user
     */
    public void registerUser(String newuser, String newpass) {
        // Generate salt
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);

        String usersalt = encodeHex(saltBytes, 32); // salt
        String passhash = getHash(newpass, usersalt); // hashed password
        System.out.println(usersalt);

        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.REGISTER_SQL);
                statement.setString(1, newuser);
                statement.setString(2, passhash);
                statement.setString(3, usersalt);
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Deleted a user by reviewId and hotelId from database
     * @param reviewId  String
     * @param hotelId  String
     */
    public void deleteReview(String reviewId,String hotelId) {
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = dbConnection.prepareStatement(PreparedStatements.DELETE_REVIEW_FOR_USER);
                statement.setString(1, reviewId);
                statement.setString(2, hotelId);
                statement.executeUpdate();
                statement.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Authenticate a user to check if the username and password matches with the one in database
     * @param username  String
     * @param password  String
     */
    public boolean authenticateUser(String username, String password) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            //System.out.println("dbConnection successful");
            statement = connection.prepareStatement(PreparedStatements.AUTH_SQL);
            String usersalt = getSalt(connection, username);
            String passhash = getHash(password, usersalt);

            statement.setString(1, username);
            statement.setString(2, passhash);
            ResultSet results = statement.executeQuery();
            boolean flag = results.next();
            return flag;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * Checks if the user already exists in table
     * If yes it will return true else false
     * @param username  String
     */
    public boolean getUser(String username) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.GET_USER_BY_USERNAME);
            statement.setString(1, username);
            ResultSet results = statement.executeQuery();
            boolean flag = results.next();
            return flag;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * Gets the salt for a specific user.
     *
     * @param connection - active database connection
     * @param user       - which user to retrieve salt for
     * @return salt for the specified user or null if user does not exist
     * @throws SQLException if any issues with database connection
     */
    private String getSalt(Connection connection, String user) {
        String salt = null;
        try (PreparedStatement statement = connection.prepareStatement(PreparedStatements.SALT_SQL)) {
            statement.setString(1, user);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                salt = results.getString("usersalt");
                return salt;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return salt;
    }

    /**
     * Method to create hotels table in database
     */
    public void createHotelsTable() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.createStatement();
            statement.executeUpdate(PreparedStatements.CREATE_HOTELS_TABLE);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Method to insert data into hotels table
     * @param hotelList  List<Hotel>
     */
    public void insertIntoHotelsTable(List<Hotel> hotelList) {
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            for (Hotel hotel : hotelList) {
                try {
                    statement = dbConnection.prepareStatement(PreparedStatements.INSERT_INTO_HOTELS);
                    statement.setString(1, hotel.getHotelId());
                    statement.setString(2, hotel.getHotelName());
                    statement.setString(3, hotel.getAddress());
                    statement.setString(4, hotel.getLatitude());
                    statement.setString(5, hotel.getLongitude());
                    statement.setString(6, hotel.getCity());
                    statement.setString(7, hotel.getState());
                    statement.setString(8, hotel.getCountry());
                    statement.executeUpdate();
                    statement.close();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Method to get a list of hotels consisting the searched keyword
     * @param keyword  String
     */
    public List<Hotel> getHotelNamesByKeyword(String keyword){
        PreparedStatement statement;
        Hotel hotel = null;
        String word = "%"+keyword+"%";
        List<Hotel> hotelList = new ArrayList<>();
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
                statement = dbConnection.prepareStatement(PreparedStatements.GET_HOTELS_BY_KEYWORD);
                statement.setString(1, word);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String hotelId = resultSet.getString("id");
                    String hotelName = resultSet.getString("name");
                    String hotelAddress = resultSet.getString("address");
                    String hotelLatitude = resultSet.getString("latitude");
                    String hotelLongitude = resultSet.getString("longitude");
                    String hotelCity = resultSet.getString("city");
                    String hotelState = resultSet.getString("state");
                    String hotelCountry = resultSet.getString("country");
                    hotel = new Hotel(hotelId, hotelName, hotelAddress, hotelLatitude, hotelLongitude, hotelCity, hotelState, hotelCountry);
                    hotelList.add(hotel);
                }
                statement.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        return  hotelList;
    }

    /**
     * Method to get a hotel from table by its name
     * @param hotelName  String
     */
    public Hotel getHotelByName(String hotelName){
        PreparedStatement statement;
        Hotel hotel = null;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.prepareStatement(PreparedStatements.GET_HOTEL_BY_NAME);
            statement.setString(1, hotelName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String hotelId = resultSet.getString("id");
                hotelName = resultSet.getString("name");
                String hotelAddress = resultSet.getString("address");
                String hotelLatitude = resultSet.getString("latitude");
                String hotelLongitude = resultSet.getString("longitude");
                String hotelCity = resultSet.getString("city");
                String hotelState = resultSet.getString("state");
                String hotelCountry = resultSet.getString("country");
                hotel = new Hotel(hotelId, hotelName, hotelAddress, hotelLatitude, hotelLongitude, hotelCity, hotelState, hotelCountry);
            }
            statement.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return  hotel;
    }

    /**
     * Method to get a hotel by its id
     * @param hotelId  String
     */
    public Hotel getHotelById(String hotelId){
        PreparedStatement statement;
        Hotel hotel = null;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.prepareStatement(PreparedStatements.GET_HOTEL_BY_ID);
            statement.setString(1, hotelId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                 hotelId = resultSet.getString("id");
                String hotelName = resultSet.getString("name");
                String hotelAddress = resultSet.getString("address");
                String hotelLatitude = resultSet.getString("latitude");
                String hotelLongitude = resultSet.getString("longitude");
                String hotelCity = resultSet.getString("city");
                String hotelState = resultSet.getString("state");
                String hotelCountry = resultSet.getString("country");
                hotel = new Hotel(hotelId, hotelName, hotelAddress, hotelLatitude, hotelLongitude, hotelCity, hotelState, hotelCountry);
            }
            statement.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return  hotel;
    }

    /**
     * Method to get a list of reviews related with the given hotelId
     * @param hotelId  String
     */
    public List<Review> getReviewsById(String hotelId){
        PreparedStatement statement;
        Review review = null;
        List<Review> reviewList = new ArrayList<>();
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.prepareStatement(PreparedStatements.GET_REVIEWS_BY_HOTEL_ID);
            statement.setString(1, hotelId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String reviewId = resultSet.getString("review_id");
                double rating = (Double.parseDouble(resultSet.getString("overall_rating")));
                String title = resultSet.getString("title");
                String reviewText = resultSet.getString("review_text");
                String userName = resultSet.getString("username");
                String date = resultSet.getString("submission_date");
                hotelId = resultSet.getString("hotel_id");
                review = new Review(reviewId,rating,title,reviewText,userName,date.trim(),hotelId);
                System.out.println(review.toString());
                reviewList.add(review);
            }
            statement.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return  reviewList;
    }

    /**
     * Method to get a list of reviews given by the user and hotelId
     * @param username  String
     * @param  hotelId String
     */
    public List<Review> getReviewsForUser(String hotelId,String username){
        PreparedStatement statement;
        Review review = null;
        List<Review> reviewList = new ArrayList<>();
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.prepareStatement(PreparedStatements.GET_REVIEWS_FOR_USER);
            statement.setString(1, hotelId);
            statement.setString(2, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String reviewId = resultSet.getString("review_id");
                double rating = (Double.parseDouble(resultSet.getString("overall_rating")));
                String title = resultSet.getString("title");
                String reviewText = resultSet.getString("review_text");
                username = resultSet.getString("username");
                String date = resultSet.getString("submission_date");
                hotelId = resultSet.getString("hotel_id");
                review = new Review(reviewId,rating,title,reviewText,username,date.trim(),hotelId);
                System.out.println(review.toString());
                reviewList.add(review);
            }
            statement.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return  reviewList;
    }

    /**
     * Method to get a review by hotelId and reviewId
     * @param hotelId  String
     * @param  reviewId String
     */
    public Review getReviewsByReviewIdAndHotelId(String hotelId,String reviewId){
        PreparedStatement statement;
        Review review = null;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.prepareStatement(PreparedStatements.GET_REVIEWS_BY_HOTEL_ID_AND_REVIEW_ID);
            statement.setString(1, hotelId);
            statement.setString(2, reviewId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                reviewId = resultSet.getString("review_id");
                double rating = (Double.parseDouble(resultSet.getString("overall_rating")));
                String title = resultSet.getString("title");
                String reviewText = resultSet.getString("review_text");
                String username = resultSet.getString("username");
                String date = resultSet.getString("submission_date");
                hotelId = resultSet.getString("hotel_id");
                review = new Review(reviewId,rating,title,reviewText,username,date.trim(),hotelId);
                System.out.println(review.toString());
            }
            statement.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return review;
    }

    /**
     * Method to update the review details after user edits the reviews
     * @param review Review
     */
    public void updateReviewDetails(Review review) {
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = dbConnection.prepareStatement(PreparedStatements.UPDATE_REVIEW_DETAILS);
                statement.setString(1, review.getTitle());
                statement.setString(2, review.getReviewText());
                statement.setDouble(3, review.getRatingOverall());
                statement.setString(4, review.getDate());
                statement.setString(5, review.getUserNickname());
                statement.setString(6, review.getReviewId());
                statement.setString(7, review.getHotelId());
                statement.executeUpdate();
                statement.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Method to create reviews table
     */
    public void createReviewsTable() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.createStatement();
            statement.executeUpdate(PreparedStatements.CREATE_REVIEWS_TABLE);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Method to insert data into reviews table
     * @param reviewList  List<List<Review>>
     */
    public void insertIntoReviewsTable(List<List<Review>> reviewList) {
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            for(List<Review> reviews : reviewList) {
                for (Review review : reviews) {
                    try {
                        statement = dbConnection.prepareStatement(PreparedStatements.INSERT_INTO_REVIEWS);
                        statement.setString(1, review.getReviewId());
                        statement.setDouble(2, review.getRatingOverall());
                        statement.setString(3, review.getTitle());
                        statement.setString(4, review.getReviewText());
                        statement.setString(5, review.getUserNickname());
                        statement.setString(6, review.getDate().toString());
                        statement.setString(7, review.getHotelId());
                        statement.executeUpdate();
                        statement.close();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Method to add data into reviews table after user tries to add new review
     * @param reviewList List<Review>
     */
    public void insertIntoReviews(List<Review> reviewList) {
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
                for (Review review : reviewList) {
                    try {
                        statement = dbConnection.prepareStatement(PreparedStatements.INSERT_INTO_REVIEWS);
                        statement.setString(1, review.getReviewId());
                        statement.setDouble(2, review.getRatingOverall());
                        statement.setString(3, review.getTitle());
                        statement.setString(4, review.getReviewText());
                        statement.setString(5, review.getUserNickname());
                        statement.setString(6, review.getDate().toString());
                        statement.setString(7, review.getHotelId());
                        statement.executeUpdate();
                        statement.close();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }

            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Method to get average of all the reviews associated with the given hotelId
     * @param hotelId  String
     */
    public double getAverageReviews(String hotelId){
        PreparedStatement statement;
        double averageRating = 0.0;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.prepareStatement(PreparedStatements.GET_AVERAGE_REVIEWS);
            statement.setString(1, hotelId);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                averageRating = Double.parseDouble(resultSet.getString("overall_rating"));
                System.out.println("Average Rating: " + averageRating);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return  averageRating;
    }

    public void createLoginTable() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.createStatement();
            statement.executeUpdate(PreparedStatements.CREATE_LOGIN_TABLE);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void insertLastLoginDetails(String userid, String lastLogin) {
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = dbConnection.prepareStatement(PreparedStatements.INSERT_INTO_LOGIN_HISTORY);
                statement.setString(1, userid);
                statement.setString(2, lastLogin);
                statement.executeUpdate();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    public void updateLastLoginDetails(String userid, String lastLogin) {
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = dbConnection.prepareStatement(PreparedStatements.UPDATE_LOGIN_HISTORY);
                statement.setString(1, lastLogin);
                statement.setString(2, userid);
                statement.executeUpdate();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public String getLastLoginDetails(String userid) {
        PreparedStatement statement;
        String lastLogin = "N/A";
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = dbConnection.prepareStatement(PreparedStatements.GET_LOGIN_DETAILS);
                statement.setString(1, userid);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    lastLogin = resultSet.getString("last_login");
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return lastLogin;
    }
    public String getUserByName(String username){
        PreparedStatement statement;
        String userid = "";
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.prepareStatement(PreparedStatements.GET_USER_BY_NAME);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userid = resultSet.getString("userid");
            }
            statement.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return  userid;
    }

    public void createExpediaHistoryTable() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.createStatement();
            statement.executeUpdate(PreparedStatements.CREATE_EXPEDIA_HISTORY_TABLE);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }


    public void insertExpediaHistory(String userid, String hotelId, String hotelName, String hotelLink) {
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = dbConnection.prepareStatement(PreparedStatements.INSERT_INTO_EXPEDIA_HISTORY_TABLE);
                statement.setString(1, userid);
                statement.setString(2, hotelId);
                statement.setString(3, hotelName);
                statement.setString(4, hotelLink);
                statement.setInt(5, 1);
                statement.executeUpdate();
                statement.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public int getExpediaPageVisitCount(String userid, String hotelId) {
        PreparedStatement statement;
        int visitCount = 0;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = dbConnection.prepareStatement(PreparedStatements.GET_EXPEDIA_VISIT_COUNT);
                statement.setString(1, userid);
                statement.setString(2, hotelId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    visitCount = resultSet.getInt("visit_count");
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return visitCount;
    }

    public void updateExpediaHistory(String userid, String hotelId) {
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = dbConnection.prepareStatement(PreparedStatements.UPDATE_EXPEDIA_HISTORY_TABLE);
                statement.setString(1, userid);
                statement.setString(2, hotelId);
                statement.executeUpdate();
                statement.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void deleteExpediaHistory(String userid) {
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = dbConnection.prepareStatement(PreparedStatements.DELETE_FROM_EXPEDIA_HISTORY_TABLE);
                statement.setString(1, userid);
                statement.executeUpdate();
                statement.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }
    public void deleteFavoriteHotels(String userid) {
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = dbConnection.prepareStatement(PreparedStatements.DELETE_FROM_FAVORITE_HOTEL_TABLE);
                statement.setString(1, userid);
                statement.executeUpdate();
                statement.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    public Set<Review> getLimitedReviews(String hotelId, String limit, String offset) {
        Set<Review> reviewSet = new HashSet<>();
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = dbConnection.prepareStatement(PreparedStatements.GET_LIMITED_REVIEWS);
                statement.setString(1, hotelId);
                statement.setInt(2, Integer.parseInt(limit));
                statement.setInt(3, Integer.parseInt(offset));
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String reviewId = resultSet.getString("review_id");
                    double rating = (Double.parseDouble(resultSet.getString("overall_rating")));
                    String title = resultSet.getString("title");
                    String reviewText = resultSet.getString("review_text");
                    String userName = resultSet.getString("username");
                    String date = resultSet.getString("submission_date");
                    hotelId = resultSet.getString("hotel_id");
                    reviewSet.add(
                            new Review(reviewId,rating,title,reviewText,userName,date.trim(),hotelId)
                    );
                }
            } catch (Exception ex) {
                System.out.println(ex);
                reviewSet = null;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            reviewSet = null;
        }
        return reviewSet;

    }

    public void createFavouriteHotelTable() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            statement = dbConnection.createStatement();
            statement.executeUpdate(PreparedStatements.CREATE_FAVORITE_HOTEL_TABLE);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public List<String> getFavouriteHotel(String userid,String hotelId){
        List<String> hotelList =new ArrayList<>();
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.prepareStatement(PreparedStatements.GET_FAVOURITE_HOTEL);
            statement.setString(1, hotelId);
            statement.setString(2, userid);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                hotelId = resultSet.getString("id");
                String hotelName = resultSet.getString("name");
                String hotelAddress = resultSet.getString("address");
                String hotelLatitude = resultSet.getString("latitude");
                String hotelLongitude = resultSet.getString("longitude");
                String hotelCity = resultSet.getString("city");
                String hotelState = resultSet.getString("state");
                String hotelCountry = resultSet.getString("country");
                String user = resultSet.getString("user");
                hotelList.add(hotelId);
                hotelList.add(hotelName);
                hotelList.add(hotelAddress);
                hotelList.add(hotelLatitude);
                hotelList.add(hotelLongitude);
                hotelList.add(hotelCity);
                hotelList.add(hotelState);
                hotelList.add(hotelCountry);
                hotelList.add(user);
            }
            statement.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return  hotelList;
    }

    public List<List<String>> getAllFavouriteHotels(String userid){
        List<List<String>> hList = new ArrayList<>();
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.prepareStatement(PreparedStatements.GET_ALL_FAVOURITE_HOTELS);
            statement.setString(1, userid);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String hotelId = resultSet.getString("id");
                String hotelName = resultSet.getString("name");
                String hotelAddress = resultSet.getString("address");
                String hotelLatitude = resultSet.getString("latitude");
                String hotelLongitude = resultSet.getString("longitude");
                String hotelCity = resultSet.getString("city");
                String hotelState = resultSet.getString("state");
                String hotelCountry = resultSet.getString("country");
                String user = resultSet.getString("user");
                List<String> hotelList =new ArrayList<>();
                hotelList.add(hotelId);
                hotelList.add(hotelName);
                hotelList.add(hotelAddress);
                hotelList.add(hotelLatitude);
                hotelList.add(hotelLongitude);
                hotelList.add(hotelCity);
                hotelList.add(hotelState);
                hotelList.add(hotelCountry);
                hotelList.add(user);
                hList.add(hotelList);
            }
            statement.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return  hList;
    }

    public List<List<String>> getExpediaVisitHistory(String userid) {
        List<List<String>> visitHistory = new ArrayList<>();
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = dbConnection.prepareStatement(PreparedStatements.GET_EXPEDIA_HISTORY);
                statement.setString(1, userid);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String hotelName = resultSet.getString("hotel_name");
                    String hotelLink = resultSet.getString("hotel_link");
                    String visitCount = resultSet.getString("visit_count");
                    List<String> hotelPageVisitDetails = new ArrayList<>();
                    hotelPageVisitDetails.add(hotelName);
                    hotelPageVisitDetails.add(hotelLink);
                    hotelPageVisitDetails.add(visitCount);
                    visitHistory.add(hotelPageVisitDetails);
                }

            } catch (Exception ex) {
                System.out.println(ex);
                visitHistory = null;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            visitHistory = null;
        }
        return visitHistory;
    }
    public void insertIntoFavouriteHotelsTable(String userid,Hotel hotel) throws SQLException {
        PreparedStatement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
                try {
                    statement = dbConnection.prepareStatement(PreparedStatements.INSERT_INTO_FAVOURITE_HOTELS);
                    statement.setString(1, hotel.getHotelId());
                    statement.setString(2, hotel.getHotelName());
                    statement.setString(3, hotel.getAddress());
                    statement.setString(4, hotel.getLatitude());
                    statement.setString(5, hotel.getLongitude());
                    statement.setString(6, hotel.getCity());
                    statement.setString(7, hotel.getState());
                    statement.setString(8, hotel.getCountry());
                    statement.setString(9, userid);
                    statement.executeUpdate();
                    statement.close();
                } catch (Exception ex) {
                    System.out.println(ex);

            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}