package cn.tingjiangzuo.handler.wcoffee;

import java.text.ParseException;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.tingjiangzuo.FunctionUtil;
import cn.tingjiangzuo.handler.GenericHandler;

public class WCoffeeTitleHandler extends GenericHandler {

	private String secondElementName;

	private int numLayersForRecording;

	public WCoffeeTitleHandler(String parsedElementName,
			String attrName, String attrValue, String secondElementName) {
		super(parsedElementName, attrName, attrValue, "title");
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
		String str = new String(ch, start, length);
		str = str.replaceAll("\n", " ");
		str = str.replaceAll("\\s{1,}", " ");
		stringBuilder.append(str);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);
		if (secondElementName.equalsIgnoreCase(localName)) {
			--numLayersForRecording;
		}
	}

}
