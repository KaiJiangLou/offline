package cn.tingjiangzuo.handler.dahuodong;

import java.util.Map;

import org.apache.tika.metadata.Metadata;

import cn.tingjiangzuo.handler.GenericHandler;

public class DaHuoDongTitleHandler extends GenericHandler {

	private Metadata metadata;
	
	public DaHuoDongTitleHandler(Metadata metadata) {
		super("", "", "", "title");
		this.metadata = metadata;
	}

	public Map<String, String> getParsedResults() {
		if (!resultingMap.isEmpty()) {
			return resultingMap;
		}
		String titleString = metadata.get(Metadata.TITLE);
		String[] terms = titleString.split("_");
		resultingMap.put(resultingKeyString, terms[0]);
		return resultingMap;
	}
}
