package cn.tingjiangzuo.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.json.JSONStringer;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class CsdnCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	/*
	 * public static final String CRAWL_STORAGE_FOLDER =
	 * "/Users/king/Documents/WhatIHaveDone/KaiJiangLou/csdn"; public static
	 * final String DATA_STORAGE_FOLDER =
	 * "/Users/king/Documents/WhatIHaveDone/KaiJiangLou/csdn/data"; public
	 * static final String SITE_PREFIX = "http://huiyi.csdn.net/meeting/info";
	 * public static final String SITE_SEED = "http://huiyi.csdn.net/meeting";
	 */

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
				&& href.startsWith(getLinkFollowingSitePrefix());
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.print("URL: " + url);
		if (!url.startsWith(getLinkStoringSitePrefix())) {
			System.out.println("");
			return;
		}
		System.out.println("...... visited!");

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			List<WebURL> links = htmlParseData.getOutgoingUrls();

			/*
			 * System.out.println("Text length: " + text.length());
			 * System.out.println("Html length: " + html.length());
			 * System.out.println("Number of outgoing links: " + links.size());
			 */
			// System.out.println(htmlParseData.getText());
			try {
				writeToFile(page, htmlParseData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void writeToFile(Page page, HtmlParseData htmlParseData)
			throws Exception {
		File dataDir = new File(getDataStorageFolder());
		if (!dataDir.exists()) {
			if (!dataDir.mkdir()) {
				throw new Exception("Couldn't create this folder: "
						+ dataDir.getAbsolutePath());
			}
		}
		JSONStringer theWebPage = new JSONStringer();
		theWebPage.object().key("url").value(page.getWebURL().getURL())
				.key("content").value(htmlParseData.getHtml()).endObject();
		String fileName = page.getWebURL().getPath();
		fileName = fileName.replaceAll("/", "_");
		File dataFile = new File(dataDir, fileName);
		BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
		writer.write(theWebPage.toString());
		writer.close();
	}

	private String getLinkFollowingSitePrefix() {
		return getCustomData(Controller.LINK_FOLLOWING_PREFIX_KEY_NAME);
	}

	private String getLinkStoringSitePrefix() {
		return getCustomData(Controller.LINK_STORING_PREFIX_KEY_NAME);
	}

	private String getDataStorageFolder() {
		return getCustomData(Controller.DATA_STORAGE_FOLDER_KEY_NAME);
	}

	@SuppressWarnings("unchecked")
	private String getCustomData(String key) {
		return ((Map<String, String>) getMyController().getCustomData())
				.get(key);
	}
}
