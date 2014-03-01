package cn.tingjiangzuo;

import org.xml.sax.Attributes;

public class CsdnAddressHandler extends AbstractBaseHandler {

	public CsdnAddressHandler(String parsedElementName) {
		super(parsedElementName);
	}

	@Override
	public boolean attributesMatched(String uri, String localName, String name,
			Attributes atts) {
			if ("class".equals(atts.getLocalName(0))
					&& "adress".equals(atts.getValue("class"))) {
				return true;
			}
		return false;
	}

}
