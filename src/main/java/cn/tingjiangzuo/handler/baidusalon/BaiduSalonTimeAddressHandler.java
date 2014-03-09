package cn.tingjiangzuo.handler.baidusalon;

import java.text.ParseException;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.tingjiangzuo.FunctionUtil;

public class BaiduSalonTimeAddressHandler extends BaiduSalonContentHandler {

	private String secondElementName;
	private int numLayersForRecording;

	public BaiduSalonTimeAddressHandler(String parsedElementName,
			String attrName, String attrValue, String secondElementName) {
		super(parsedElementName, attrName, attrValue, "");
		this.secondElementName = secondElementName;
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		super.startElement(uri, localName, name, atts);
		if (numLayers > 0) {
			if (secondElementName.equalsIgnoreCase(localName)) {
				numLayersForRecording = 1;
			} else if (numLayersForRecording > 0) {
				++numLayersForRecording;
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (numLayersForRecording <= 0) {
			return;
		}
		stringBuilder.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);
		if (secondElementName.equalsIgnoreCase(localName)) {
			--numLayersForRecording;
		}
		if (numLayersForRecording <= 0) {
			stringBuilder.append("\n");
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
			} else if (line.startsWith("地址：")) {
				resultingMap.put("address", line.substring("时间：".length()));
			}
		}
		return resultingMap;
	}

	/**
	 * Parse string like "时间：14:00～17:30 ，2014年3月15日（周六）".
	 * @param line
	 */
	private void parseStartAndEndTime(String line) {
		String[] startAndEndTime = line.split("[,，]");
		if (startAndEndTime.length < 2) {
			return;
		}
		String firstPartString = startAndEndTime[0].trim();
		String[] hourMinuteStrings = firstPartString.split("[~～]");
		if (hourMinuteStrings.length < 2) {
			return;
		}
		String startTimeString = startAndEndTime[1].trim() + hourMinuteStrings[0].trim();
		String endTimeString = startAndEndTime[1].trim() + hourMinuteStrings[1].trim();

		try {
			resultingMap.put("start_time",
					"" + FunctionUtil.parseDateString2TS(startTimeString));
			resultingMap.put("end_time",
					"" + FunctionUtil.parseDateString2TS(endTimeString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
