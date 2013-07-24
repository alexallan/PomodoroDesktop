package utils;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Functions {

	
	/**
	 * Turns a date in milliseconds into a string in the format dd-mm-yyyy and with the uk timezone
	 * @param timeInMillis time in milliseconds
	 * @return
	 */
	public static String turnDateInMillisToString( long timeInMillis)
	{
		
	
        // "yyyy-MM-dd HH:mm:ss,SSS",Locale.UK);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy",Locale.UK);

        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UK"));
        calendar.setTimeInMillis(timeInMillis);
       // System.out.println("GregorianCalendar -"+sdf.format(calendar.getTime()));
        String output = sdf.format(calendar.getTime());
		return output;
	}

}
