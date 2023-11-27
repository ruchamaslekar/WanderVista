package server;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

    public void createHotelsTable() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.createStatement();
            statement.executeUpdate(PreparedStatements.CREATE_HOTELS_TABLE);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

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

    public void createReviewsTable() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.createStatement();
            statement.executeUpdate(PreparedStatements.CREATE_REVIEWS_TABLE);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

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
}