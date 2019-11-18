package fvarrui.cuestionarios.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static Date parseDate(String strDate, String format) throws ParseException {
		if (strDate == null || strDate.isEmpty()) return null;
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(strDate);
	}
	
	public static String formatDate(Date date, String format) throws ParseException {
		if (date == null) return "";
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
	
}
