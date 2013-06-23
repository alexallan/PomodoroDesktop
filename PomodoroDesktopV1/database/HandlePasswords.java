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
	private static String password;

	/**
	 * Attempts to connect to the database using the given password - returns
	 * true if it worked
	 * 
	 * @return
	 */
	public static boolean checkIfRightPW(String pw) {
		return DatabaseQuerys.testConnection(pw);
	}

	/**
	 * @return the password
	 */
	public static String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public static void setPassword(String password) {
		HandlePasswords.password = password;
	}

	/**
	 * checks for the existance of password file
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
		
		return bufferedReader.readLine();
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

}
