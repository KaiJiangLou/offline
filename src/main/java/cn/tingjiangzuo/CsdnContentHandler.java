package cn.tingjiangzuo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CsdnContentHandler extends AbstractBaseHandler {

	public CsdnContentHandler(String parsedElementName) {
		super(parsedElementName);
	}

	@Override
	public boolean attributesMatched(String uri, String localName, String name,
			Attributes atts) {
		if ("class".equals(atts.getLocalName(0))
				&& "con_nr_l".equals(atts.getValue("class"))) {
			return true;
		}
		return false;
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		super.startElement(uri, localName, name, atts);
	}
}
