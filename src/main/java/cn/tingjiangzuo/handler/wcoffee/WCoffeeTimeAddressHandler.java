package cn.tingjiangzuo.handler.wcoffee;

import java.text.ParseException;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.tingjiangzuo.FunctionUtil;
import cn.tingjiangzuo.handler.GenericHandler;

public class WCoffeeTimeAddressHandler extends GenericHandler {

	public WCoffeeTimeAddressHandler(String parsedElementName,
			String attrName, String attrValue) {
		super(parsedElementName, attrName, attrValue, "");
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		super.startElement(uri, localName, name, atts);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (numLayers <= 0) {
			return;
		}
		String str = new String(ch, start, length);
		str = str.replaceAll("\n", " ");
		str = str.replaceAll("\\s{1,}", " ");
		stringBuilder.append(str);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (numLayers > 0 && "br".equalsIgnoreCase(localName)) {
			stringBuilder.append("\n");
		}
		super.endElement(uri, localName, name);
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

	/**
	 * Parse string like "04月01日 14:00 -  17:30 ".
	 * 
	 * @param line
	 */
	private void parseStartAndEndTime(String line) {
		String[] startAndEndTime = line.trim().split("-");
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
