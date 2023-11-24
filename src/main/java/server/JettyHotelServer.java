package server;

import hotelData.*;
import reviewData.*;
import servlets.HomeServlet;
import servlets.HotelSearchServlet;
import servlets.LoginServlet;
import servlets.RegistrationServlet;

import java.io.PrintWriter;
import java.util.List;

public class JettyHotelServer {
	public static final int PORT = 8080;

	public static void main(String[] args) throws Exception {
		// FILL IN CODE, and add more classes as needed
//		ProgramArgumentParser programParser = new ProgramArgumentParser();
//		programParser.parseArgs(args);
//		/** Loading data for Hotel */
//		HotelParser parser = new HotelParser();
//		HotelDetails hotelDetails = new ThreadSafeHotelDetails();
//		parser.parseHotelJson(programParser.getArgument("-hotels"),hotelDetails);
//		List<Hotel> list = parser.parseHotelJson(programParser.getArgument("-hotels"),hotelDetails);
//
//		/** Loading data for Reviews */
//		DirectoryParser directoryParser = new DirectoryParser();
//		List<List<Review>> reviewList = directoryParser.parseDirectory(programParser.getArgument("-reviews"));
//
//		/** Creating and inserting into database */
//		DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
//		databaseHandler.createTable();
//		databaseHandler.createHotelsTable();
//		databaseHandler.insertIntoHotelsTable(list);
//		databaseHandler.createReviewsTable();
//		databaseHandler.insertIntoReviewsTable(reviewList);

		JettyServer server = new JettyServer();
		server.addMapping("/login", LoginServlet.class);
		server.addMapping("/register", RegistrationServlet.class);
		server.addMapping("/home", HomeServlet.class);
		server.addMapping("/hotels", HotelSearchServlet.class);
		server.start();
	}
}