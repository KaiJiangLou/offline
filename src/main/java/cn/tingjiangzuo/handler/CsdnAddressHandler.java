package cn.tingjiangzuo.handler;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CsdnAddressHandler extends AbstractBaseHandler {

	private boolean alreadyRead;

	public CsdnAddressHandler(String parsedElementName) {
		super(parsedElementName, "");
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
				resultingMap.put("address", line.substring("时间：".length()));
			}
		}
		return resultingMap;
	}

	private void parseStartAndEndTime(String line) {
		String[] startAndEndTime = line.split("-");
		String startTimeString = startAndEndTime[0].trim();
		String endTimeString = "~";
		if (startAndEndTime.length > 1) {
			endTimeString = startAndEndTime[1].trim();
		}
		if (endTimeString.length() < startTimeString.length()) {
			endTimeString = startTimeString.substring(0,
					startTimeString.length() - endTimeString.length())
					+ endTimeString;
		}
		resultingMap.put("start_time", startTimeString);
		resultingMap.put("end_time", endTimeString);
	}
}