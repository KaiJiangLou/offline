package cn.tingjiangzuo;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONStringer;
import org.junit.Test;

public class JsonTest {

	@Test
	public void testEmptyString() throws JSONException {
		String str = "";
		assertTrue(str.isEmpty());
		assertTrue(!str.equals(" "));
	}

	@Test
	public void testJson() throws JSONException {
		JSONStringer jsonStringer = new JSONStringer();
		jsonStringer.object().key("a").value("b \"': ").key("b")
				.value("sdfa\n dsddd1").endObject();
		System.out.println(jsonStringer.toString());
	}

	@Test
	public void testJsonArray() throws JSONException {
		Map<String, String> theMap = new HashMap<>();
		theMap.put("a", "a-value");
		theMap.put("b", "b-value");
		JSONArray jsonArray = new JSONArray(new Map[]{theMap, theMap});
		System.out.println(jsonArray.toString());
	}
}
