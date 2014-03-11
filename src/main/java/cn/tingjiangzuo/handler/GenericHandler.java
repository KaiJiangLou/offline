package cn.tingjiangzuo.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.tika.sax.ContentHandlerDecorator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class GenericHandler extends ContentHandlerDecorator {

	protected String parsedElementName;
	protected String parsedAttributeName;
	protected String parsedAttributeValue;

	protected String resultingKeyString;

	// protected boolean begin = false;
	protected int numLayers;
	protected StringBuilder stringBuilder = new StringBuilder();

	protected Map<String, String> resultingMap = new HashMap<>();

	public GenericHandler(String parsedElementName, String parsedAttributeName,
			String parsedAttributeValue, String resultingKeyString) {
		this.parsedElementName = parsedElementName;
		this.parsedAttributeName = parsedAttributeName;
		this.parsedAttributeValue = parsedAttributeValue;
		this.resultingKeyString = resultingKeyString;
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		if (parsedElementName.equalsIgnoreCase(localName)) {
			if (attributesMatched(uri, localName, name, atts)) {
				numLayers = 1;
			} else if (numLayers > 0) {
				++numLayers;
			}
		}
	}

	/**
	 * A hook function, which can be overridden by subclasses.
	 * 
	 * @return
	 */
	public boolean attributesMatched(String uri, String localName, String name,
			Attributes atts) {
		if ((atts == null || atts.getLength() == 0)
				&& (parsedAttributeName == null || parsedAttributeName
						.isEmpty())) {
			return true;
		}
		if (atts == null) {
			return false;
		}
		if ((atts.getLength() > 0 && parsedAttributeName.equals(atts
				.getLocalName(0)))
				&& atts.getValue(parsedAttributeName).startsWith(
						parsedAttributeValue)) {
			return true;
		}
		return false;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (numLayers <= 0) {
			return;
		}
		stringBuilder.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (parsedElementName.equalsIgnoreCase(localName)) {
			--numLayers;
		}
	}

	/**
	 * A hook function, which should only be called when the whole webpage is
	 * parsed entirely.
	 * 
	 * @return
	 */
	public Map<String, String> getParsedResults() {
		if (!resultingMap.isEmpty()) {
			return resultingMap;
		}
		String contentString = stringBuilder.toString().trim();
		if (contentString.length() > 0) {
			resultingMap.put(resultingKeyString, contentString);
		}
		return resultingMap;
	}

	@Override
	public String toString() {
		return getParsedResults().toString();
	}

}
