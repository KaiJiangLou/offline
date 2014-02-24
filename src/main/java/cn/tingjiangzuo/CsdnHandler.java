package cn.tingjiangzuo;

import java.awt.datatransfer.StringSelection;

import org.apache.tika.sax.ContentHandlerDecorator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CsdnHandler extends ContentHandlerDecorator {

	private boolean begin = false;
	private StringBuilder stringBuilder = new StringBuilder();

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		if ("div".equalsIgnoreCase(localName)) {
			if ("class".equals(atts.getLocalName(0))
					&& "adress".equals(atts.getValue("class"))) {
				begin = true;
			} else {
				begin = false;
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (!begin) {
			return;
		}
		stringBuilder.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if ("div".equalsIgnoreCase(localName)) {
			begin = false;
		}
	}

	@Override
	public String toString() {
		String str = stringBuilder.toString();
		String[] lines = str.split("\n");
		StringBuilder newBuilder = new StringBuilder();
		for (String line : lines) {
			newBuilder.append(line.trim() + "\n");
		}
		return newBuilder.toString();
	}
}
