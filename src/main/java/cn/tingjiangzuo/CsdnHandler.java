package cn.tingjiangzuo;

import java.util.List;

import org.apache.tika.sax.ContentHandlerDecorator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CsdnHandler extends ContentHandlerDecorator {

	private List<AbstractBaseHandler> handlers;

	public CsdnHandler(List<AbstractBaseHandler> handlers) {
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

	@Override
	public String toString() {
	StringBuilder stringBuilder = new StringBuilder();
		for (AbstractBaseHandler handler : handlers) {
			stringBuilder.append(handler.getClass().getName() + ":\n");
			stringBuilder.append(handler.toString());
			stringBuilder.append("-------------------------------------------");
		}
		return stringBuilder.toString();
	}
}
