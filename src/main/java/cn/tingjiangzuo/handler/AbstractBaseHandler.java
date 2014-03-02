package cn.tingjiangzuo.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.tika.sax.ContentHandlerDecorator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class AbstractBaseHandler extends ContentHandlerDecorator {

	protected String parsedElementName;
	protected String resultingKeyString;
	
	// protected boolean begin = false;
	protected int numLayers;
	protected StringBuilder stringBuilder = new StringBuilder();

	protected Map<String, String> resultingMap = new HashMap<>();

	public AbstractBaseHandler(String parsedElementName, String resultingKeyString) {
		this.parsedElementName = parsedElementName;
		this.resultingKeyString = resultingKeyString;
	}

	public abstract boolean attributesMatched(String uri, String localName,
			String name, Attributes atts);

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
	 * A hook function, which should only be called when the whole webpage is parsed entirely.
	 * @return
	 */
	public Map<String, String> getParsedResults() {
		if (!resultingMap.isEmpty()) {
			return resultingMap;
		}
		resultingMap.put(resultingKeyString, stringBuilder.toString().trim());
		return resultingMap;
	}

	@Override
	public String toString() {
		return getParsedResults().toString();
	}


}
