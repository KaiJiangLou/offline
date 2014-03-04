package cn.tingjiangzuo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.junit.Test;

public class DateFormatTest {

	@Test
	public void testDataFormat() throws JSONException, ParseException {
		String[] dateStrs = new String[] { "2012年3月2日", "2012年3月2日13点",
				"2012年3月2日13:20", "2012年3月2日3:20", "2012年3月2日13:20:52" };
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (String dateStr : dateStrs) {
			Date date = FunctionUtil.str2Data(dateStr);
			System.out.println(sdf.format(date));
		}
	}
}
