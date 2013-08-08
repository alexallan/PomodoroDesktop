package database;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.WillyTask;
import tables.CreateTable;

import com.sun.org.apache.bcel.internal.Constants;

import core.GlobalConstants;

public class DatabaseQuerys {

	/** Defines if we want the connection information to be printed */
	private final static boolean verbose = true;

	/**
	 * Tries to open a db connection, returns null if this cant be done
	 * 
	 * @param username
	 *            the db username
	 * @param password
	 *            the db password
	 * @return db connection if one can be made, null if not
	 */
	private static Connection openDBConnection(String username, String password) {
		// Register the JDBC driver for MySQL.
		try {
			Class.forName(GlobalConstants.DB_MSQLREGISTRY);

			// Define URL of database server for
			// database named mysql on the localhost
			// with the default port number 3306.
			String url = GlobalConstants.DB_URL;

			// Get a connection to the database for a
			// user named root with a blank password.
			// This user is the default administrator
			// having full privileges to do anything.
			Connection con;

			con = DriverManager.getConnection(url, username, password);
			if (verbose) {
				// Display URL and connection information
				System.out.println("URL: " + url);
				System.out.println("Connection established successfully: "
						+ con);
			}
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();

			return null;
		}

	}

	/**
	 * Establishes connection to database TODO make this happen in a separate
	 * thread -
	 */
	public static boolean testConnection(String username, String password) {

		Statement stmt;

		// Do nothing in this test app - just check connection
		Connection con = openDBConnection(username, password);

		// Tidy up
		if (verbose) {
			System.out.println("Closing connection: " + con);
		}
		try { // try and close connection - will throw the catch if this cant be
				// done
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/***
	 * Gets the contents of the specified table. Choose on of the final strings
	 * in the {@link GlobalConstants} class as the table name
	 * 
	 * @param tableName
	 * @return a results set object
	 */
	public synchronized static ObservableList<WillyTask> pullTaskTable() {
		ResultSet tableContents = null;
		ObservableList<WillyTask> data = null;
		try {

			Statement stmt;

			// open a db connection
			Connection con = openDBConnection(GlobalConstants.DB_USERNAME,
					HandlePasswords.getPassword());

			// try and get the table
			stmt = con.createStatement();
			String query = "select * from " + GlobalConstants.TABLE_NAME_TASK
					+ " ;";
			tableContents = stmt.executeQuery(query);

			// put the database results object in an observable list
			data = FXCollections.observableArrayList();

			try {
				while (tableContents.next()) {
					// add a new task from the result object

					String name = tableContents
							.getString(GlobalConstants.COL_TASKNAME);
					int spent = tableContents
							.getInt(GlobalConstants.COL_NUM_SPENT);
					long date = tableContents.getDate(
							GlobalConstants.COL_DATE_CREATED).getTime();
					int uid = tableContents.getInt(GlobalConstants.COL_UID);
					data.add(new WillyTask(name, spent, date, uid));

				}
			} catch (SQLException e) {

				e.printStackTrace();
			}

			if (verbose) {
				// Tidy up
				System.out.println("Closing connection: " + con);
			}
			con.close();
		} catch (Exception e) {
			return null;
		}
		return data;
	}

	public synchronized static boolean updateTask(WillyTask task) {
		try {
			Statement stmt;

			// Register the JDBC driver for MySQL.
			Class.forName(GlobalConstants.DB_MSQLREGISTRY);

			// Define URL of database server for
			// database named mysql on the localhost
			// with the default port number 3306.
			String url = GlobalConstants.DB_URL;

			// Get a connection to the database for a
			// user named root with a blank password.
			// This user is the default administrator
			// having full privileges to do anything.
			Connection con = DriverManager.getConnection(url,
					GlobalConstants.DB_USERNAME, HandlePasswords.getPassword());

			// Display URL and connection information
			System.out.println("URL: " + url);
			System.out.println("Connection established successfully: " + con);

			// This is how the sql should appear
			// UPDATE taskTable
			// SET taskName='fury wank', numberOfPomodorosSpent='2'
			// WHERE taskName='fury wank';
			//
			// build the sql update statement
			String update = "UPDATE " + GlobalConstants.TABLE_NAME_TASK;
			String set = " SET taskName = '" + task.taskNameProperty().get()
					+ "', numberOfPomodorosSpent = "
					+ String.valueOf(task.completedPomsProperty().get());
			String where = " WHERE taskName = '"
					+ task.taskNameProperty().get() + "';";
			String updateStatement = update + set + where;

			// send the statement to the db
			stmt = con.createStatement();
			stmt.executeUpdate(updateStatement);

			// Tidy up
			System.out.println("Closing connection: " + con);
			con.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Puts a new task in the database - does so using a separate thread
	 * 
	 * @param task
	 */
	public synchronized static void newTaskInSeparateThread(WillyTask task) {
		// you have to make task final here so the thread can access it and it
		// wont be fucked with while its being processed by that thread
		final WillyTask taskToAdd = task;
		new Thread(new Runnable() {
			// Starts a new thread
			public void run() {

				newTask(taskToAdd);
			}
		}).start();
	}

	/**
	 * Adds a new task to the database
	 * 
	 * @param task
	 *            the task to add
	 * @return
	 */
	public synchronized static boolean newTask(WillyTask task) {
		try {
			Statement stmt;

			// Register the JDBC driver for MySQL.
			Class.forName(GlobalConstants.DB_MSQLREGISTRY);

			// Define URL of database server for
			// database named mysql on the localhost
			// with the default port number 3306.
			String url = GlobalConstants.DB_URL;

			// Get a connection to the database for a
			// user named root with a blank password.
			// This user is the default administrator
			// having full privileges to do anything.
			Connection con = DriverManager.getConnection(url,
					GlobalConstants.DB_USERNAME, HandlePasswords.getPassword());

			// Display URL and connection information
			System.out.println("URL: " + url);
			System.out.println("Connection established successfully: " + con);

			// This is how the sql should appear
			// INSERT INTO table1
			// (uniqueId,taskName,numberOfPomodorosSpent,dateCreated)
			// VALUES ('','Lush n00dz', 0,CURRENT_TIMESTAMP);
			//
			// build the sql update statement
			String insert = "INSERT INTO " + GlobalConstants.TABLE_NAME_TASK
					+ "(" + "taskName" + "," + "numberOfPomodorosSpent" + ","
					+ "dateCreated" + ")";
			String values = " VALUES  ('" + task.taskNameProperty().get()
					+ "'," + "0,CURRENT_TIMESTAMP);";
			String updateStatement = insert + values;

			// send the statement to the db
			stmt = con.createStatement();
			stmt.executeUpdate(updateStatement);

			// update the table
			CreateTable.updateTableFromDB();
			
			// Tidy up
			System.out.println("Closing connection: " + con);
			con.close();

		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
