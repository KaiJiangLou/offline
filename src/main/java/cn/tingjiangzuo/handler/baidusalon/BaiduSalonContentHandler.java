package cn.tingjiangzuo.handler.baidusalon;

import org.xml.sax.Attributes;

import cn.tingjiangzuo.handler.AbstractBaseHandler;

public class BaiduSalonContentHandler extends AbstractBaseHandler {

	private String attributeName;
	private String attributeValue;

	public BaiduSalonContentHandler(String parsedElementName,
			String attrName,
			String attrValue, String resultingKeyString) {
		super(parsedElementName, resultingKeyString);
		this.attributeName = attrName;
		this.attributeValue = attrValue;
	}

	@Override
	public boolean attributesMatched(String uri, String localName, String name,
			Attributes atts) {
		if (attributeName.equals(atts.getLocalName(0))
				&& atts.getValue(attributeName).startsWith(attributeValue)) {
			return true;
		}
		return false;
	}

}
