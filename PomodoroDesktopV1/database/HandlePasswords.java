package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class HandlePasswords {

	/** The password for the database*/
	private static String password = null;

	/** The username to set*/
	private static String username = null;
	
	
	/**
	 * Attempts to connect to the database using the given password - returns
	 * true if it worked
	 * 
	 * @return
	 */
	public static boolean checkIfRightPW(String uName, String pw) {
		return DatabaseQuerys.testConnection(uName,pw);
	}

	/**
	 * @return the password
	 */
	public static String getPassword() {
		return password;
	}

	
	/**
	 * Checks for the existence of password file and makes it the active password if it finds one
	 * 
	 * @return returns a the password if it exists, null otherwise
	 */
	public static String checkForPasswordFile()

	{
		String filePath = new File("").getAbsolutePath();
		filePath += "\\config\\pw.scrote";

		FileReader fileReader;
		try {
			fileReader = new FileReader(filePath);
		
		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		String password =  bufferedReader.readLine();
		// gives the password to the handle passwords class
		HandlePasswords.password = password;
		
		return password;
		}
		 catch (FileNotFoundException e) {
				return null; // the password could not be found
			} catch (IOException e) {
				return null; // the file was fucked up
		}
	}
	
	/** Writes an existing password to file
	 * @return false if there was a problem*/
	public static boolean writePwFile(String pw)
	{
		try {
			 
			String filePath = new File("").getAbsolutePath();
			filePath += "\\config\\pw.scrote";
	
			File file = new File(filePath);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(pw);
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param dbUsername the username to set
	 */					
	public static void setUsername(String dbUsername) {

		username = dbUsername;
		
	}
	/**
	 * @param password
	 *            the password to set
	 */
	public static void setPassword(String password) {
		HandlePasswords.password = password;
	}
	
	/**
	 * Checks if the password we last tried was correct
	 * @return true if it was
	 */
	public static boolean gotCorrectPW()
	{
		if (password == null)
			return false;
		
		return true;
	}


}
