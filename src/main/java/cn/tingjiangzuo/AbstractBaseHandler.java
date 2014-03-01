package cn.tingjiangzuo;

import org.apache.tika.sax.ContentHandlerDecorator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class AbstractBaseHandler extends ContentHandlerDecorator {

	protected String parsedElementName;
	protected boolean begin = false;
	protected int numLayers;
	protected StringBuilder stringBuilder = new StringBuilder();

	public AbstractBaseHandler (String parsedElementName) {
		this.parsedElementName = parsedElementName;
	}

	public abstract boolean attributesMatched(String uri, String localName, String name,
			Attributes atts);
	
	@Override
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		System.out.println(String.format("startElement: uri = %s,  localName = %s, name = %s.", uri, localName, name));
		if (parsedElementName.equalsIgnoreCase(localName)) {
			if (attributesMatched(uri, localName, name, atts)) {
				begin = true;
				numLayers = 0;
			}
			if (begin) {
				++numLayers;
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
		System.out.println(String.format("endElement: uri = %s,  localName = %s, name = %s.", uri, localName, name));
		if (parsedElementName.equalsIgnoreCase(localName)) {
			--numLayers;
			if (numLayers <= 0) {
				begin = false;
			}
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
