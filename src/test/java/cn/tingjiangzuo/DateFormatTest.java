package cn.tingjiangzuo;

import java.text.ParseException;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.junit.Test;

public class DateFormatTest {

	@Test
	public void testDataFormat() throws JSONException, ParseException {
		String[] dateStrs = new String[] { "2012年3月2日", "2012年3月2日13点",
				"2012年3月2日13:20", "2012年3月2日3:20", "2012年3月2日13:20:52" };
		String[] expectedResults = new String[] { "2012-03-02 00:00",
				"2012-03-02 13:00", "2012-03-02 13:20", "2012-03-02 03:20",
				"2012-03-02 13:20" };
		String[] realResults = new String[expectedResults.length];
		int i = 0;
		for (String dateStr : dateStrs) {
			realResults[i++] = FunctionUtil.parseDateString(dateStr);
		}
		assertArrayEquals(expectedResults, realResults);
	}
}
