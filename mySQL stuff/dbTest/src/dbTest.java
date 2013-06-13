import java.sql.*;

public class dbTest {
 public static void main(String args[]){
 try {
 Statement stmt;

 //Register the JDBC driver for MySQL.
 Class.forName("com.mysql.jdbc.Driver");

 //Define URL of database server for
 // database named mysql on the localhost
 // with the default port number 3306.
 String url =
 "jdbc:mysql://aws.6p-milk.com:3306/pomodoro";

 //Get a connection to the database for a
 // user named root with a blank password.
 // This user is the default administrator
 // having full privileges to do anything.
 Connection con =
 DriverManager.getConnection(
 url,"pomodoro", "Pi11ag3 Gubbinz");

 //Display URL and connection information
 System.out.println("URL: " + url);
 System.out.println("Connection established successfully: " + con);

 //Do nothing in this test app
 
 //Tidy up
 System.out.println("Closing connection: " + con);
 con.close();
 }catch( Exception e ) {
 e.printStackTrace();
 }//end catch
 }//end main
}//end class dbTest