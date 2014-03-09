package cn.tingjiangzuo.handler;

import java.util.List;

import cn.tingjiangzuo.handler.baidusalon.BaiduSalonContentHandler;
import cn.tingjiangzuo.handler.baidusalon.BaiduSalonTimeAddressHandler;
import cn.tingjiangzuo.handler.csdn.CsdnContentHandler;
import cn.tingjiangzuo.handler.csdn.CsdnTimeAddressHandler;
import cn.tingjiangzuo.handler.csdn.CsdnTitleHandler;

import com.google.common.collect.Lists;

public class ExtractorUtils {

	enum WebSite {
		CSDN, BaiduSalon
	}

	public static String getInputDir(WebSite webSite) {
		switch (webSite) {
		case CSDN:
			return "/Users/king/Documents/WhatIHaveDone/KaiJiangLou/csdn/data";
		case BaiduSalon:
			return "/Users/king/Documents/WhatIHaveDone/KaiJiangLou/baidu_salon/data";
		default:
			return "";
		}
	}

	public static List<AbstractBaseHandler> getHandlers(WebSite webSite) {
		List<AbstractBaseHandler> handlers = Lists.newArrayList();
		switch (webSite) {
		case CSDN:
			handlers.add(new CsdnTitleHandler("div", "h1", "title"));
			handlers.add(new CsdnTimeAddressHandler("div"));
			handlers.add(new CsdnContentHandler("div", "div", "intro",
					"content"));
			break;
		case BaiduSalon:
			//handlers.add(new BaiduSalonContentHandler("h1", "class", "general", "title"));
			handlers.add(new BaiduSalonContentHandler("div", "style", "width", "content"));
			//handlers.add(new BaiduSalonTimeAddressHandler("div", "style", "width", "p"));
			break;

		default:
			break;
		}
		return handlers;
	}
}
