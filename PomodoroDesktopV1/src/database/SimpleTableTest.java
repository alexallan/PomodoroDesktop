package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SimpleTableTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		

	}
	
	
	/** Establishes connection to database TODO make this happen in a seperate thread - */
	public static boolean establishConnection(String password)
	{
		
		
		try {
			Statement stmt;

			// Register the JDBC driver for MySQL.
			Class.forName("com.mysql.jdbc.Driver");

			// Define URL of database server for
			// database named mysql on the localhost
			// with the default port number 3306.
			String url = "jdbc:mysql://aws.6p-milk.com:3306/pomodoro";

			// Get a connection to the database for a
			// user named root with a blank password.
			// This user is the default administrator
			// having full privileges to do anything.
			Connection con = DriverManager.getConnection(url, "pomodoro",
					password);

			// Display URL and connection information
			System.out.println("URL: " + url);
			System.out.println("Connection established successfully: " + con);

			// Do nothing in this test app

			// Tidy up
			System.out.println("Closing connection: " + con);
			con.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
