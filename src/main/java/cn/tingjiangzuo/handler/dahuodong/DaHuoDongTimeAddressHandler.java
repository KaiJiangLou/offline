package cn.tingjiangzuo.handler.dahuodong;

import java.text.ParseException;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.tingjiangzuo.FunctionUtil;
import cn.tingjiangzuo.handler.GenericHandler;

public class DaHuoDongTimeAddressHandler extends GenericHandler {

	private String secondElementName;
	private String secondAttributeName;
	private String secondAttributeValue;

	private int numLayersForRecording;

	public DaHuoDongTimeAddressHandler(String parsedElementName,
			String attrName, String attrValue, String secondElementName,
			String secondAttrName, String secondAttrValue) {
		super(parsedElementName, attrName, attrValue, "");
		this.secondElementName = secondElementName;
		this.secondAttributeName = secondAttrName;
		this.secondAttributeValue = secondAttrValue;
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		super.startElement(uri, localName, name, atts);
		if (numLayers > 0) {
			if (secondElementName.equalsIgnoreCase(localName)) {
				if (shouldRecord(uri, localName, name, atts)) {
					numLayersForRecording = 1;
				} else if (numLayersForRecording > 0) {
					++numLayersForRecording;
				}
			}
		}
	}

	private boolean shouldRecord(String uri, String localName, String name,
			Attributes atts) {
		if (secondAttributeName.equals(atts.getLocalName(0))
				&& atts.getValue(secondAttributeName).startsWith(
						secondAttributeValue)) {
			return true;
		}
		return false;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (numLayersForRecording <= 0) {
			return;
		}
		String str = new String(ch, start, length);
		str = str.replaceAll("\n", " ");
		str = str.replaceAll("\\s{1,}", " ");
		// stringBuilder.append(ch, start, length);
		stringBuilder.append(str);
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
			if (line.startsWith("活动时间：")) {
				parseStartAndEndTime(line.substring("活动时间：".length()));
			} else if (line.startsWith("活动地址：")) {
				resultingMap.put("address", line.substring("活动地址：".length()));
			}
		}
		return resultingMap;
	}

	/**
	 * Parse string like "时间：14:00～17:30 ，2014年3月15日（周六）".
	 * 
	 * @param line
	 */
	private void parseStartAndEndTime(String line) {
		String[] startAndEndTime = line.split("[-]");
		if (startAndEndTime.length < 2) {
			return;
		}
		try {
			int timeStamp = FunctionUtil.parseDateString2TS(startAndEndTime[0]);
			resultingMap.put("start_time", "" + timeStamp);
			timeStamp = FunctionUtil.parseDateString2TS(startAndEndTime[1]);
			resultingMap.put("end_time", "" + timeStamp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
