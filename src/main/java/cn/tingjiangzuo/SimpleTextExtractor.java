package cn.tingjiangzuo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.feed.FeedParser;
import org.apache.tika.parser.html.DefaultHtmlMapper;
import org.apache.tika.parser.html.HtmlMapper;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class SimpleTextExtractor {

	public static void main(String[] args) throws Exception {
		/*
		 * // Create a Tika instance with the default configuration Tika tika =
		 * new Tika();
		 * 
		 * // Parse all given files and print out the extracted text content for
		 * (String file : args) { String text = tika.parseToString(new
		 * File(file)); System.out.print(text); }
		 */
		File file = new File(
				"/Users/king/Documents/WhatIHaveDone/TingJiangZuo/csdn/data/_meeting_info_674_community");
		System.out.println(fileToTxt(file));
	}

	public static String fileToTxt(File f) {
		Parser parser = new AutoDetectParser();
		InputStream is = null;
		try {
			Metadata metadata = new Metadata();
			metadata.set(Metadata.AUTHOR, "空号");
			metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
			is = new FileInputStream(f);
			// ContentHandler handler = new BodyContentHandler();
			ContentHandler handler = new CsdnHandler();
			ParseContext context = new ParseContext();
			context.set(Parser.class, parser);
			context.set(HtmlMapper.class, new MyHtmlMapper());
			parser.parse(is, handler, metadata, context);
			for (String name : metadata.names()) {
				System.out.println(name + ":" + metadata.get(name));
			}
			return handler.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
		return null;
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
			/*if ("BR".equalsIgnoreCase(name)) {
				return "br";
			}*/
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
