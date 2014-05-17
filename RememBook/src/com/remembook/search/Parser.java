package com.remembook.search;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

public class Parser {
	private String key1;
	ArrayList<BookData> data;

	Parser(String key) {
		this.key1 = key;
	}

	public ArrayList<BookData> getBookData(final String info, final int count,
			final int start) {
		data = new ArrayList<BookData>();

		BookData item = null;

		String m_searchinfo = "";

		try {
			m_searchinfo = URLEncoder.encode(info, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		try {
			URL text = new URL("http://openapi.naver.com/search?key=" + key1
					+ "&query=" + m_searchinfo + "&display=" + count
					+ "&start=" + start + "&target=book");
			XmlPullParserFactory parserCreator = XmlPullParserFactory
					.newInstance();
			XmlPullParser parser = parserCreator.newPullParser();

			parser.setInput(text.openStream(), null);

			Log.i("NET", "Parsing");

			int parseEvent = parser.getEventType();
			while (parseEvent != XmlPullParser.END_DOCUMENT) {

				switch (parseEvent) {
				case XmlPullParser.START_TAG:
					String tag = parser.getName();

					if (tag.compareTo("title") == 0) {
						item = new BookData();
						Spanned mtitle = Html.fromHtml(parser.nextText());
						String titleStr = mtitle.toString();
						item.title = titleStr;
						Log.i("NET", "START");
					}
					if (tag.compareTo("image") == 0) {
						String imageSrc = parser.nextText();
						item.image = imageSrc;
					}
					if (tag.compareTo("author") == 0) {
						Spanned mauthor = Html.fromHtml(parser.nextText());
						String authorStr = mauthor.toString();
						item.author = authorStr;
					}
					if (tag.compareTo("publisher") == 0) {
						Spanned mpublisher = Html.fromHtml(parser.nextText());
						String publisherStr = mpublisher.toString();
						item.publisher = publisherStr;
					}
					if (tag.compareTo("isbn") == 0) {
						String isbnSrc = parser.nextText();
						item.isbn = isbnSrc;
						data.add(item);
					}
					break;
				}
				parseEvent = parser.next();
			}
			Log.i("NET", "End...");
		} catch (Exception e) {
			Log.i("NET", "Parsing fail");
		}
		return data;
	}
}
