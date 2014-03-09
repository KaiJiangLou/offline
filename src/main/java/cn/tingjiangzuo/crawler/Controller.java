package cn.tingjiangzuo.crawler;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

	public static String CRAWL_STORAGE_FOLDER_KEY_NAME = "crawl_storage_folder";
	public static String DATA_STORAGE_FOLDER_KEY_NAME = "data_storage_folder";
	public static String SITE_PREFIX_KEY_NAME = "site_prefix";
	public static String SITE_SEED_KEY_NAME = "site_seed";
	public static String MAX_DEPTH_CRAWLING_KEY_NAME = "max_depth_crawling";
	public static String LIVE_KEY_NAME = "live";

	public static void main(String[] args) throws Exception {
		File configFile = new File("kjl_source.json");
		JSONArray sourceArray = new JSONArray(new JSONTokener(
				new FileReader(configFile)));
		int length = sourceArray.length();
			System.out.println(sourceArray.toString());
		for (int i = 0; i < length; ++i) {
			crawlOneWebsite(sourceArray.getJSONObject(i));
		}
	}
	
	private static void crawlOneWebsite(JSONObject websiteInfo) throws Exception {
		if (!websiteInfo.has(LIVE_KEY_NAME) || !websiteInfo.getBoolean(LIVE_KEY_NAME)) {
			return;
		}
		String crawlStorageFolder = websiteInfo.getString(CRAWL_STORAGE_FOLDER_KEY_NAME);
		String siteSeed = websiteInfo.getString(SITE_SEED_KEY_NAME);
		int maxDepthCrawling = websiteInfo.getInt(MAX_DEPTH_CRAWLING_KEY_NAME);
		String dataStorageFolder = websiteInfo.getString(DATA_STORAGE_FOLDER_KEY_NAME);
		String sitePrefix = websiteInfo.getString(SITE_PREFIX_KEY_NAME);

		int numberOfCrawlers = 3;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxDepthOfCrawling(maxDepthCrawling);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher,
				robotstxtServer);

		Map<String, String> otherDataMap = new HashMap<>();
		otherDataMap.put(DATA_STORAGE_FOLDER_KEY_NAME, dataStorageFolder);
		otherDataMap.put(SITE_PREFIX_KEY_NAME, sitePrefix);
		controller.setCustomData(otherDataMap);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		controller.addSeed(siteSeed);

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(CsdnCrawler.class, numberOfCrawlers);
	}
}
