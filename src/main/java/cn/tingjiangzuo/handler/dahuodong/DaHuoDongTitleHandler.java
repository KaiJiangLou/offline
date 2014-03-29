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
		String title = terms[0];
		if (title.endsWith("会议门票价格")) {
			title = title.substring(0, title.length() - "会议门票价格".length());
		}
		resultingMap.put(resultingKeyString, title);
		return resultingMap;
	}
}
