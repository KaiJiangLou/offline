package cn.tingjiangzuo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CsdnTitleHandler extends AbstractBaseHandler {

	private String secondElementName;
	
	public CsdnTitleHandler(String parsedElementName, String secondElementName) {
		super(parsedElementName);
		this.secondElementName = secondElementName;
	}

	@Override
	public boolean attributesMatched(String uri, String localName, String name,
			Attributes atts) {
		if ("class".equals(atts.getLocalName(0))
				&& "event_tit".equals(atts.getValue("class"))) {
			return true;
		}
		return false;
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		System.out.println(String.format("startElement: uri = %s,  localName = %s, name = %s.", uri, localName, name));
		if (parsedElementName.equalsIgnoreCase(localName)) {
			if (attributesMatched(uri, localName, name, atts)) {
				begin = true;
				numLayers = 1;
			}
		} else if (begin && secondElementName.equalsIgnoreCase(localName)) {
			numLayers = 2;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (numLayers != 2) {
			return;
		}
		stringBuilder.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		System.out.println(String.format("endElement: uri = %s,  localName = %s, name = %s.", uri, localName, name));
		if (parsedElementName.equalsIgnoreCase(localName) || secondElementName.equalsIgnoreCase(localName)) {
			begin = false;
			numLayers = 0;
		}
	}
}
