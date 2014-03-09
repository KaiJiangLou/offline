package cn.tingjiangzuo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

public class FunctionUtil {

	public static int parseDateString2TS(String dateStr) throws ParseException {
		Date date = FunctionUtil.parsDateString2Date(dateStr);
		return (int) (date.getTime() / 1000);
	}

	/**
	 * Parse a non-regular date string to a regular date string with format "yyyy-MM-dd HH:mm".
	 * <br> For example, <br> 
	 * "2012年3月2日" will return "2012-03-02 00:00"; <br> 
	 * "2012年3月2日13点" will return "2012-03-02 13:00"; <br> 
	 * "2012年3月2日13:20" will return "2012-03-02 13:20"; <br> 
	 * "2012年3月2日13:20:50" will return "2012-03-02 13:20". <br> 
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static String parseDateString(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = FunctionUtil.parsDateString2Date(dateStr);
		return sdf.format(date);
	}

	/**
	 * Parse a date string to a {@link Date}.
	 * 
	 * @param theDateStr
	 * @return
	 * @throws ParseException
	 */
	public static Date parsDateString2Date(String theDateStr)
			throws ParseException {
		String sepRegEx = "[^0-9]";
		Pattern pattern = Pattern.compile(sepRegEx);
		String[] terms = pattern.split(theDateStr);
		terms = removeEmptyElements(terms);
		String formatStr = "yyyy-MM-dd";
		String dateStr = String
				.format("%s-%s-%s", terms[0], terms[1], terms[2]);
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
		return date;
	}

	private static String[] removeEmptyElements(String[] terms) {
		List<String> nonEmptyList = Lists.newArrayList();
		for (String term : terms) {
			if (!term.isEmpty()) {
				nonEmptyList.add(term);
			}
		}
		String[] resultStrings = new String[nonEmptyList.size()];
		resultStrings = nonEmptyList.toArray(resultStrings);
		return resultStrings;
	}
}
