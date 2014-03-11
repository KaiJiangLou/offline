package cn.tingjiangzuo.handler.dahuodong;

import java.util.List;
import java.util.Map;

import cn.tingjiangzuo.handler.CompositeHandler;
import cn.tingjiangzuo.handler.GenericHandler;

public class DaHuoDongCompositeHandler extends CompositeHandler {

	private String filterKeyName;
	private String keySubstring;

	public DaHuoDongCompositeHandler(List<GenericHandler> handlers,
			String filterKeyName, String keySubstring) {
		super(handlers);
		this.filterKeyName = filterKeyName;
		this.keySubstring = keySubstring;
	}

	public Map<String, String> getParsedResults() {
		if (!resultingMap.isEmpty()) {
			return resultingMap;
		}
		super.getParsedResults();
		if (shouldFilter()) {
			resultingMap.clear();
		}
		resultingMap.remove(filterKeyName);
		return resultingMap;
	}

	private boolean shouldFilter() {
		String priceString = resultingMap.get(filterKeyName);
		if (priceString == null || !priceString.contains(keySubstring)) {
			return true;
		}
		return false;
	}

}
