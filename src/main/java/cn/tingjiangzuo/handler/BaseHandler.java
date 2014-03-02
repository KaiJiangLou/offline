package cn.tingjiangzuo.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tika.sax.ContentHandlerDecorator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class BaseHandler extends ContentHandlerDecorator {

	private List<AbstractBaseHandler> handlers;

	private Map<String, String> resultingMap = new HashMap<>();

	public BaseHandler(List<AbstractBaseHandler> handlers) {
		this.handlers = handlers;
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		for (AbstractBaseHandler handler : handlers) {
			handler.startElement(uri, localName, name, atts);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		for (AbstractBaseHandler handler : handlers) {
			handler.characters(ch, start, length);
		}
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		for (AbstractBaseHandler handler : handlers) {
			handler.endElement(uri, localName, name);
		}
	}

	public Map<String, String> getParsedResults() {
		if (!resultingMap.isEmpty()) {
			return resultingMap;
		}
		for (AbstractBaseHandler handler : handlers) {
			resultingMap.putAll(handler.getParsedResults());
		}
		return resultingMap;
	}
	
	@Override
	public String toString() {
		return getParsedResults().toString();
	}
}
