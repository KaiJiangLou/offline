package cn.tingjiangzuo;

import java.text.ParseException;
import java.util.Arrays;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.junit.Test;

public class DateFormatTest {

	@Test
	public void testDataFormatTS() throws JSONException, ParseException {
		String[] dateStrs = new String[] { "2012年3月2日", "2012年3月2日13点",
				"2012年3月2日13:20", "2012年3月2日3:20", "2012年3月2日13:20:52" };
		int[] expectedResults = new int[] { 1330617600, 1330664400, 1330665600,
				1330629600, 1330665652 };
		int[] realResults = new int[expectedResults.length];
		int i = 0;
		for (String dateStr : dateStrs) {
			realResults[i++] = FunctionUtil.parseDateString2TS(dateStr);

		}
		System.out.println(Arrays.toString(realResults));
		assertArrayEquals(expectedResults, realResults);
	}

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
