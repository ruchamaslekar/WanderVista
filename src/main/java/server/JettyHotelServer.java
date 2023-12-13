package server;

import reviewData.DirectoryParser;
import reviewData.Review;
import servlets.*;
import hotelData.*;
import java.io.IOException;
import java.util.List;

public class JettyHotelServer {
	public static final int PORT = 8080;

	public static void main(String[] args) throws Exception {
		// FILL IN CODE, and add more classes as needed
//		databaseHelper(args);



		JettyServer server = new JettyServer();
		server.addMapping("/login", LoginServlet.class);
		server.addMapping("/register", RegistrationServlet.class);
		server.addMapping("/home", HomeServlet.class);
		server.addMapping("/hotelSearch", HotelSearchServlet.class);
		server.addMapping("/hotels", HotelServlet.class);
		server.addMapping("/hotelDetails", HotelDetailsServlet.class);
		server.addMapping("/fetchReviews", ReviewDetailsServlet.class);
		server.addMapping("/addReviews", AddReviewsServlet.class);
		server.addMapping("/deleteReviews", DeleteReviewsServlet.class);
		server.addMapping("/editReviews", EditReviewsServlet.class);
		server.addMapping("/expediaHistory", ExpediaHistoryServlet.class);
		server.addMapping("/logout", LogoutServlet.class);
		server.start();
	}

	public static void databaseHelper(String[] args) throws IOException {
		ProgramArgumentParser programParser = new ProgramArgumentParser();
		programParser.parseArgs(args);
		/** Loading data for Hotel */
		HotelParser parser = new HotelParser();
		HotelDetails hotelDetails = new ThreadSafeHotelDetails();
		parser.parseHotelJson(programParser.getArgument("-hotels"),hotelDetails);
		List<Hotel> list = parser.parseHotelJson(programParser.getArgument("-hotels"),hotelDetails);
		/** Loading data for Reviews */
		DirectoryParser directoryParser = new DirectoryParser();
		List<List<Review>> reviewList = directoryParser.parseDirectory(programParser.getArgument("-reviews"));
		/** Creating and inserting into database */
		DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
		databaseHandler.createExpediaHistoryTable();
		databaseHandler.createTable();
		databaseHandler.createHotelsTable();
		databaseHandler.insertIntoHotelsTable(list);
		databaseHandler.createReviewsTable();
		databaseHandler.insertIntoReviewsTable(reviewList);
		databaseHandler.createLoginTable();
	}
}