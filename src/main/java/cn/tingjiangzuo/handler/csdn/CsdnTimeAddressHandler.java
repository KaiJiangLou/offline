package cn.tingjiangzuo.handler.csdn;

import java.text.ParseException;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.tingjiangzuo.FunctionUtil;
import cn.tingjiangzuo.handler.GenericHandler;

public class CsdnTimeAddressHandler extends GenericHandler {

	private boolean alreadyRead;

	public CsdnTimeAddressHandler(String parsedElementName) {
		super(parsedElementName, "class", "adress", "");
	}

	@Override
	public boolean attributesMatched(String uri, String localName, String name,
			Attributes atts) {
		if (alreadyRead) {
			return false;
		}
		if ("class".equals(atts.getLocalName(0))
				&& "adress".equals(atts.getValue("class"))) {
			return true;
		}
		return false;
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (numLayers > 0) {
			alreadyRead = true;
		}
		if (parsedElementName.equalsIgnoreCase(localName)) {
			--numLayers;
		}
	}

	@Override
	public Map<String, String> getParsedResults() {
		if (!resultingMap.isEmpty()) {
			return resultingMap;
		}
		String[] lines = stringBuilder.toString().split("\n");
		for (String line : lines) {
			line = line.trim();
			if (line.startsWith("时间：")) {
				parseStartAndEndTime(line.substring("时间：".length()));
			} else if (line.startsWith("地点：")) {
				resultingMap.put("address", line.substring("地点：".length()));
			}
		}
		return resultingMap;
	}

	private void parseStartAndEndTime(String line) {
		String[] startAndEndTime = line.split("-");
		if (startAndEndTime.length < 2) {
			return;
		}
		String startTimeString = startAndEndTime[0].trim();
		String endTimeString = startAndEndTime[1].trim();
		if (endTimeString.length() < startTimeString.length()) {
			endTimeString = startTimeString.substring(0,
					startTimeString.length() - endTimeString.length())
					+ endTimeString;
		}

		try {
			int timeStamp = FunctionUtil.parseDateString2TS(startTimeString);
			resultingMap.put("start_time", "" + timeStamp);
			timeStamp = FunctionUtil.parseDateString2TS(endTimeString);
			resultingMap.put("end_time", "" + timeStamp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
