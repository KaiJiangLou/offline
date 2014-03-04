package cn.tingjiangzuo.handler;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.DefaultHtmlMapper;
import org.apache.tika.parser.html.HtmlMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;

import edu.uci.ics.crawler4j.url.WebURL;

public class TextFieldsExtractor {

    private static final Logger log = LoggerFactory.getLogger(TextFieldsExtractor.class);

	public static String INPUT_DIR_OPTION = "input";
	public static String OUTPUT_DIR_OPTION = "output";

	private String inputDir, outputDir;

	public static void main(String[] args) throws Exception {
		CommandLine cmdlLine = generateCommandLine(args);
		TextFieldsExtractor textFieldsExtractor = new TextFieldsExtractor();
		textFieldsExtractor.inputDir = cmdlLine
				.getOptionValue(INPUT_DIR_OPTION);
		textFieldsExtractor.outputDir = cmdlLine
				.getOptionValue(OUTPUT_DIR_OPTION);
		// Parser argsParser = new parser
		textFieldsExtractor.extractFieldsFromFiles();
	}

	private static CommandLine generateCommandLine(String[] args)
			throws ParseException {
		Options options = new Options();
		options.addOption("i", INPUT_DIR_OPTION, true,
				"the dirctory stores the files which will be parsed");
		options.addOption("o", OUTPUT_DIR_OPTION, true,
				"the dirctory stores the parsed files");
		CommandLineParser cmdLineParser = new PosixParser();
		CommandLine cmdLine = cmdLineParser.parse(options, args);
		return cmdLine;
	}

	public void extractFieldsFromFiles() throws FileNotFoundException,
			JSONException, Exception {
		File inputDir = new File(this.inputDir);
		checkInputDir(inputDir);
		File outputDir = new File(this.outputDir);
		checkOutputDir(outputDir);

		File[] inputFiles = inputDir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().startsWith(".")) {
					return false;
				}
				return true;
			}
		});

		for (File f : inputFiles) {
			log.info("Processing file {} ...", f.toString());
			try {
				Map<String, String> resultingMap = extractFieldsFromOneFile(f);
				saveToFile(resultingMap);
			} catch (Exception e) {
				System.out.println(f.getName());
				e.printStackTrace();
			}
		}
		log.info("All done.");
	}

	private void checkInputDir(File inputDir) throws FileNotFoundException {
		if (!inputDir.exists() || !inputDir.isDirectory()) {
			throw new FileNotFoundException(this.inputDir
					+ " is not a proper directory.");
		}
	}

	private void checkOutputDir(File outputDir) throws Exception {
		if (!outputDir.exists()) {
			if (!outputDir.mkdir()) {
				throw new Exception("Couldn't create this folder: "
						+ outputDir.getAbsolutePath());
			}
		}
	}

	private Map<String, String> extractFieldsFromOneFile(File f)
			throws FileNotFoundException, JSONException {
		JSONObject jsonObject = new JSONObject(new JSONTokener(
				new FileReader(f)));
		Map<String, String> resultingFieldMap = new HashMap<>();

		String content = jsonObject.getString("content");
		Parser parser = new AutoDetectParser();
		InputStream is = null;
		try {
			Metadata metadata = new Metadata();
			// metadata.set(Metadata.AUTHOR, "空号");
			// metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
			is = new ByteArrayInputStream(content.getBytes("UTF-8"));
			// ContentHandler handler = new BodyContentHandler();
			BaseHandler handler = initHandler();
			ParseContext context = new ParseContext();
			context.set(Parser.class, parser);
			context.set(HtmlMapper.class, new MyHtmlMapper());
			parser.parse(is, handler, metadata, context);
			resultingFieldMap = handler.getParsedResults();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		resultingFieldMap.put("url", jsonObject.getString("url"));
		return resultingFieldMap;
	}

	private BaseHandler initHandler() {
		List<AbstractBaseHandler> handlers = Lists.newArrayList();
		handlers.add(new CsdnTitleHandler("div", "h1", "title"));
		handlers.add(new CsdnAddressHandler("div"));
		handlers.add(new CsdnContentHandler("div", "div", "intro", "content"));
		return new BaseHandler(handlers);
	}

	private void saveToFile(Map<String, String> resultingMap)
			throws IOException {
		WebURL url = new WebURL();
		//System.out.println(resultingMap.toString());
		url.setURL(resultingMap.get("url"));
		String fileName = url.getPath();
		fileName = fileName.replaceAll("/", "_");

		File dataFile = new File(this.outputDir, fileName);
		BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
		writer.write(new JSONObject(resultingMap).toString());
		writer.close();
	}

	public static class MyHtmlMapper extends DefaultHtmlMapper {

		private Set<String> additionalSafeAttrs = new HashSet<String>();

		public MyHtmlMapper() {
			additionalSafeAttrs.add("id");
			additionalSafeAttrs.add("class");

		}

		public String mapSafeElement(String name) {
			if ("DIV".equalsIgnoreCase(name)) {
				return "div";
			}
			/*
			 * if ("BR".equalsIgnoreCase(name)) { return "br"; }
			 */
			return super.mapSafeElement(name);
		}

		public String mapSafeAttribute(String eleName, String attrName) {
			if (eleName.equals("div") && additionalSafeAttrs.contains(attrName)) {
				return attrName;
			} else {
				return super.mapSafeAttribute(eleName, attrName);
			}
		}
	}
}
