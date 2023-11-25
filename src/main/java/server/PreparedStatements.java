package server;

public class PreparedStatements {
    /** Prepared Statements  */
    /** For creating the users table */
    public static final String CREATE_USER_TABLE =
            "CREATE TABLE users (" +
                    "userid INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(32) NOT NULL UNIQUE, " +
                    "password CHAR(64) NOT NULL, " +
                    "usersalt CHAR(32) NOT NULL);";

    /** Prepared Statements  */
    /** For creating the hotels table */
    public static final String CREATE_HOTELS_TABLE = "CREATE TABLE hotels (" +
            "id varchar(64) PRIMARY KEY, " +
            "name VARCHAR(64) NOT NULL, " +
            "address VARCHAR(64) NOT NULL, " +
            "latitude VARCHAR(64) NOT NULL, " +
            "longitude VARCHAR(64) NOT NULL, " +
            "city VARCHAR(64) NOT NULL, " +
            "state VARCHAR(64) NOT NULL, " +
            "country VARCHAR(64) NOT NULL);";
    public static final String INSERT_INTO_HOTELS = "INSERT INTO hotels (id, name, address, latitude, longitude, city, state, country) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    public static final String GET_HOTELS_BY_KEYWORD =
            "SELECT * FROM hotels WHERE name like ?";

    public static final String GET_HOTEL_BY_NAME =
            "SELECT * FROM hotels WHERE name = ?";

    public static final String GET_HOTEL_BY_ID =
            "SELECT * FROM hotels WHERE id = ?";

    public static final String GET_REVIEWS_BY_HOTEL_ID=
            "SELECT * FROM reviews WHERE hotel_id = ?";

    public static final String GET_REVIEWS_For_USER=
            "SELECT * FROM reviews WHERE hotel_id = ? AND username=?";

    public static final String CREATE_REVIEWS_TABLE = "CREATE TABLE reviews(" +
            "review_id VARCHAR(64) NOT NULL, " +
            "overall_rating DOUBLE NOT NULL," +
            "title VARCHAR(64) NOT NULL, " +
            "review_text VARCHAR(2000) NOT NULL, " +
            "username VARCHAR(64) NOT NULL, " +
            "submission_date VARCHAR(64) NOT NULL, " +
            "hotel_id  VARCHAR(64) NOT NULL, " +
            "PRIMARY KEY(review_id, hotel_id), " +
            "FOREIGN KEY(hotel_id) REFERENCES hotels(id));";

    public static final String INSERT_INTO_REVIEWS = "INSERT INTO reviews(review_id, overall_rating, title, review_text, username, submission_date, hotel_id)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?);";

    public static final String GET_AVERAGE_REVIEWS = "SELECT avg(overall_rating) AS overall_rating FROM reviews WHERE hotel_id = ? GROUP BY hotel_id";

    public static final String DELETE_REVIEW_FOR_USER = "DELETE FROM reviews where review_id=? AND hotel_id=?";
    /** Used to insert a new user into the database. */
    public static final String REGISTER_SQL =
            "INSERT INTO users (username, password, usersalt) " +
                    "VALUES (?, ?, ?);";

    /** Used to retrieve the salt associated with a specific user. */
    public static final String SALT_SQL =
            "SELECT usersalt FROM users WHERE username = ?";

    /** Used to authenticate a user. */
    public static final String AUTH_SQL =
            "SELECT username FROM users " +
                    "WHERE username = ? AND password = ?";

}
