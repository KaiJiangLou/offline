package cn.tingjiangzuo.handler.csdn;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.tingjiangzuo.handler.AbstractBaseHandler;

public class CsdnContentHandler extends AbstractBaseHandler {

	private String startRecordingElementName;
	private String startRecordingElementAttrName;
	private boolean startRecording;

	public CsdnContentHandler(String parsedElementName,
			String startRecordingElementName,
			String startRecordingElementAttrName, String resultingKeyString) {
		super(parsedElementName, resultingKeyString);
		this.startRecordingElementName = startRecordingElementName;
		this.startRecordingElementAttrName = startRecordingElementAttrName;
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
		if (numLayers > 0
				&& startRecordingElementName.equalsIgnoreCase(localName)
				&& "class".equalsIgnoreCase(atts.getLocalName(0))
				&& startRecordingElementAttrName.equalsIgnoreCase(atts
						.getValue("class"))) {
			startRecording = true;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (numLayers <= 0 || !startRecording) {
			return;
		}
		stringBuilder.append(ch, start, length);
	}
}
