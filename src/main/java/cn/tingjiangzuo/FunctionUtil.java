package cn.tingjiangzuo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class FunctionUtil {

	public static Date str2Data(String theDateStr) throws ParseException {
		String sepRegEx = "[^0-9]";
		Pattern pattern = Pattern.compile(sepRegEx);
		String[] terms = pattern.split(theDateStr);
		String formatStr = "yyyy-MM-dd";
		String dateStr = String.format("%s-%s-%s", terms[0], terms[1], terms[2]);
		if (terms.length > 3) {
			formatStr += " HH";
			dateStr += " " + terms[3];
		}
		if (terms.length > 4) {
			formatStr += ":mm";
			dateStr += ":" + terms[4];
		}
		if (terms.length > 5) {
			formatStr += ":ss";
			dateStr += ":" + terms[5];
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = sdf.parse(dateStr);
		//System.out.println(date.toString());
		return date;
		
		/*String dateStr = new String(theDateStr);
		String yearStr = parseYMDString(dateStr, "年");
		String monthStr = parseYMDString(dateStr, "月");
		String dayStr = parseYMDString(dateStr, "日");
		if (!dateStr.isEmpty()) {
			
		}*/
	}
	
	private static String parseYMDString(String dateStr, String separator) {
		int yearEndIndex = dateStr.indexOf(separator);
		String resultStr = dateStr.substring(0, yearEndIndex);
		dateStr = dateStr.substring(yearEndIndex + 1);
		return(resultStr);
	}
}
