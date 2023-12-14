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

    /** Prepared Statements  */
    /** For inserting the hotels table */
    public static final String INSERT_INTO_HOTELS = "INSERT INTO hotels (id, name, address, latitude, longitude, city, state, country) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    /** Prepared Statements  */
    /** For fetching values from hotels table by keyword*/
    public static final String GET_HOTELS_BY_KEYWORD =
            "SELECT * FROM hotels WHERE name like ?";

    /** Prepared Statements  */
    /** For fetching values from hotels table by name*/
    public static final String GET_HOTEL_BY_NAME =
            "SELECT * FROM hotels WHERE name = ?";

    /** Prepared Statements  */
    /** For fetching values from hotels table by id */
    public static final String GET_HOTEL_BY_ID =
            "SELECT * FROM hotels WHERE id = ?";

    /** Prepared Statements  */
    /** For fetching values from reviews table by hotel_id*/
    public static final String GET_REVIEWS_BY_HOTEL_ID=
            "SELECT * FROM reviews WHERE hotel_id = ? ORDER BY submission_date DESC;";

    /** Prepared Statements  */
    /** For fetching values from reviews table */
    public static final String GET_REVIEWS_FOR_USER=
            "SELECT * FROM reviews WHERE hotel_id = ? AND username=?";

    /** Prepared Statements  */
    /** For fetching values from reviews table using hotel_id and review_id*/
    public static final String GET_REVIEWS_BY_HOTEL_ID_AND_REVIEW_ID=
            "SELECT * FROM reviews WHERE hotel_id = ? AND review_id=?";

    /** Prepared Statements  */
    /** For creating reviews table */
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

    /** Prepared Statements  */
    /** For inserting values into reviews table */
    public static final String INSERT_INTO_REVIEWS = "INSERT INTO reviews(review_id, overall_rating, title, review_text, username, submission_date, hotel_id)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?);";

    /** Prepared Statements  */
    /** For fetching average of reviews  from reviews table */
    public static final String GET_AVERAGE_REVIEWS = "SELECT avg(overall_rating) AS overall_rating FROM reviews WHERE hotel_id = ? GROUP BY hotel_id;";

    /** Prepared Statements  */
    /** For deleting values from reviews table */
    public static final String DELETE_REVIEW_FOR_USER = "DELETE FROM reviews where review_id=? AND hotel_id=?;";

    /** Prepared Statements  */
    /** For modifying values from reviews table */
    public static final String UPDATE_REVIEW_DETAILS = "UPDATE reviews SET title=?, review_text=?, overall_rating=?, submission_date=?,username=?  WHERE review_id=? and hotel_id=?;";

    /** Used to insert a new user into the database. */
    public static final String REGISTER_SQL =
            "INSERT INTO users (username, password, usersalt) " +
                    "VALUES (?, ?, ?);";

    /** Used to retrieve the salt associated with a specific user. */
    public static final String SALT_SQL =
            "SELECT usersalt FROM users WHERE username = ?;";

    /** Used to authenticate a user. */
    public static final String AUTH_SQL =
            "SELECT username FROM users " +
                    "WHERE username = ? AND password = ?;";

    /** Used to check if a user already exists */
    public static final String GET_USER_BY_USERNAME =
            "SELECT username FROM users " +
                    "WHERE username = ?;";

    public static final String CREATE_LOGIN_TABLE = "CREATE TABLE login_history(" +
            "userid VARCHAR(64) PRIMARY KEY, " +
            "last_login VARCHAR(64) NOT NULL " +
            "); ";

    public static final String INSERT_INTO_LOGIN_HISTORY = "INSERT INTO login_history (userid, last_login) VALUES(?,?);";

    public static final String GET_LOGIN_DETAILS = "SELECT * from login_history WHERE userid=?;";

    public static final String GET_USER_BY_NAME =
            "SELECT userid FROM users WHERE username like ?";

    public static final String UPDATE_LOGIN_HISTORY= "UPDATE login_history SET last_login=? WHERE userid=?";

    public static final String CREATE_EXPEDIA_HISTORY_TABLE = "CREATE TABLE expedia_history(" +
            "userid varchar(64) NOT NULL, " +
            "hotel_id varchar(64) NOT NULL, " +
            "hotel_name varchar(64) NOT NULL, "+
            "hotel_link varchar(1000) NOT NULL, " +
            "visit_count int NOT NULL, " +
            "PRIMARY KEY(userid, hotel_id));";
    public static final String GET_EXPEDIA_HISTORY = "SELECT * FROM expedia_history WHERE userid=? order by visit_count desc;";

    public static final String INSERT_INTO_EXPEDIA_HISTORY_TABLE = "INSERT INTO expedia_history (userid, hotel_id, hotel_name, hotel_link, visit_count) VALUES (?, ?, ?, ?, ?)";

    public static final String GET_EXPEDIA_VISIT_COUNT = "SELECT visit_count FROM expedia_history where userid=? and hotel_id=?";

    public static final String UPDATE_EXPEDIA_HISTORY_TABLE = "UPDATE expedia_history SET visit_count=visit_count+1 where userid=? and hotel_id=?";

    public static final String DELETE_FROM_EXPEDIA_HISTORY_TABLE = "DELETE FROM expedia_history WHERE userid=?;";

    public static final String GET_LIMITED_REVIEWS = "SELECT * FROM reviews WHERE hotel_id=? order by submission_date DESC limit ? offset ?;";

    public static final String CREATE_FAVORITE_HOTEL_TABLE = "CREATE TABLE favorite_hotels (" +
            "id varchar(64) PRIMARY KEY, " +
            "name VARCHAR(64) NOT NULL, " +
            "address VARCHAR(64) NOT NULL, " +
            "latitude VARCHAR(64) NOT NULL, " +
            "longitude VARCHAR(64) NOT NULL, " +
            "city VARCHAR(64) NOT NULL, " +
            "state VARCHAR(64) NOT NULL, " +
            "country VARCHAR(64) NOT NULL);";

    public static final String GET_FAVOURITE_HOTEL ="SELECT * FROM favorite_hotels where id =? AND user=?";

    public static final String GET_ALL_FAVOURITE_HOTELS ="SELECT * FROM favorite_hotels where user =?";


    public static final String INSERT_INTO_FAVOURITE_HOTELS = "INSERT INTO favorite_hotels (id, name, address, latitude, longitude, city, state, country,user) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?);";

    public static final String DELETE_FROM_FAVORITE_HOTEL_TABLE = "DELETE FROM favorite_hotels WHERE user=?;";
}

