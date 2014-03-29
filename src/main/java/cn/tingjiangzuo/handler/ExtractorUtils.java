package cn.tingjiangzuo.handler;

import java.io.File;
import java.util.List;

import org.apache.tika.metadata.Metadata;

import cn.tingjiangzuo.handler.baidusalon.BaiduSalonTimeAddressHandler;
import cn.tingjiangzuo.handler.csdn.CsdnContentHandler;
import cn.tingjiangzuo.handler.csdn.CsdnTimeAddressHandler;
import cn.tingjiangzuo.handler.csdn.CsdnTitleHandler;
import cn.tingjiangzuo.handler.dahuodong.DaHuoDongCompositeHandler;
import cn.tingjiangzuo.handler.dahuodong.DaHuoDongTimeAddressHandler;
import cn.tingjiangzuo.handler.dahuodong.DaHuoDongTitleHandler;
import cn.tingjiangzuo.handler.wcoffee.WCoffeeTimeAddressHandler;
import cn.tingjiangzuo.handler.wcoffee.WCoffeeTitleHandler;

import com.google.common.collect.Lists;

public class ExtractorUtils {

	enum WebSite {
		CSDN, BaiduSalon, DaHuoDong, WCoffee
	}

	public static String getInputDir(WebSite webSite) {
		switch (webSite) {
		case CSDN:
			return "/Users/king/Documents/WhatIHaveDone/KaiJiangLou/csdn/data";
		case BaiduSalon:
			return "/Users/king/Documents/WhatIHaveDone/KaiJiangLou/baidu_salon/data";
		case DaHuoDong:
			return "/Users/king/Documents/WhatIHaveDone/KaiJiangLou/dahuodong/data";
		case WCoffee:
			return "/Users/king/Documents/WhatIHaveDone/KaiJiangLou/wcoffee/data";
		default:
			return "";
		}
	}

	public static CompositeHandler getHandlers(WebSite webSite, Metadata metadata) {
		List<GenericHandler> handlers = Lists.newArrayList();
		CompositeHandler compositeHandler = null;
		switch (webSite) {
		case CSDN:
			handlers.add(new CsdnTitleHandler("div", "h1", "title"));
			handlers.add(new CsdnTimeAddressHandler("div"));
			handlers.add(new CsdnContentHandler("div", "div", "intro",
					"content"));
			compositeHandler = new CompositeHandler(handlers);
			break;
		case BaiduSalon:
			handlers.add(new GenericHandler("h1", "class", "general", "title"));
			handlers.add(new GenericHandler("div", "style", "width", "content"));
			handlers.add(new BaiduSalonTimeAddressHandler("div", "style", "width", "p"));
			compositeHandler = new CompositeHandler(handlers);
			break;
		case DaHuoDong:
			handlers.add(new DaHuoDongTitleHandler(metadata));
			handlers.add(new GenericHandler("div", "class", "prd_detail", "content"));
			handlers.add(new DaHuoDongTimeAddressHandler("div", "class", "prd_info", "p", "class", "f14"));
			String filterKeyName = "price";
			// for filtering out all non-free posts
			handlers.add(new GenericHandler("div", "class", "prd_pri", filterKeyName));
			compositeHandler = new DaHuoDongCompositeHandler(handlers, filterKeyName, "免费");
			break;
		case WCoffee:
			handlers.add(new WCoffeeTitleHandler("div", "class", "acti_info", "h2"));
			handlers.add(new GenericHandler("div", "class", "acti_detail", "content"));
			handlers.add(new WCoffeeTimeAddressHandler("div", "class", "acti_info"));
			compositeHandler = new CompositeHandler(handlers);
			break;

		default:
			break;
		}
		return compositeHandler;
	}
}
