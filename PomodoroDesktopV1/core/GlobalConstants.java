package core;


/**
 * This is a place to put global constant variables for example the names of columns in the database
 * @author Alex
 *
 */
public class GlobalConstants {
	
	// --*****DATABASE CONSTANTS*****----
	/** Sets up the java database driver for MySQL*/
	public static final String DB_MSQLREGISTRY = "com.mysql.jdbc.Driver";
	/** The URL of the database - uses default port number 3306*/
	public static final String DB_URL = "jdbc:mysql://aws.6p-milk.com:3306/pomodoro";
	/** The name of the database*/
	public static final String DB_NAME = "pomodoro";
	/** The username we long into the database with*/
	public static final String DB_USERNAME= "pomodoro";
	
	// *****TASK DATABASE ********//
	/** The name of the task database*/
	public static final String TABLE_NAME_TASK = "table1";
	// Task database column headers
	/** Column header for the unique ID attribute which is an integer*/
	public static final String COL_UID = "uniqueId";
	/** Column header for the task name attribute which is a string*/
	public static final String COL_TASKNAME = "taskName";
	/** Column header for the number of pomodoros spent attribute which is an integer*/
	public static final String COL_NUM_SPENT = "numberOfPomodorosSpent";
	/** Column header for the date created which is a date*/
	public static final String COL_DATE_CREATED = "dateCreated";

}
