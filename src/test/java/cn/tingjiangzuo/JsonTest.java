package cn.tingjiangzuo;

import org.json.JSONException;
import org.json.JSONStringer;
import org.junit.Test;

public class JsonTest {

	@Test
	public void testJson() throws JSONException {
		JSONStringer jsonStringer = new JSONStringer();
		jsonStringer.object().key("a").value("b \"': ").key("b")
				.value("sdfa\n dsddd1").endObject();
		System.out.println(jsonStringer.toString());
	}
}
