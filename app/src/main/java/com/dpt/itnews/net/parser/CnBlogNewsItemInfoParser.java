package com.dpt.itnews.net.parser;

import android.util.Xml;
import com.dpt.itnews.vo.CnBlogNewsItemInfo;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class CnBlogNewsItemInfoParser {

    public static final String ns = null;



    public static List<CnBlogNewsItemInfo> parse(String xmlString) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(xmlString));
            parser.nextTag();
            return readFeed(parser);
        } finally {
            // in.close();
        }
    }

    public static List<CnBlogNewsItemInfo> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private static List<CnBlogNewsItemInfo> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<CnBlogNewsItemInfo> entries = new ArrayList<CnBlogNewsItemInfo>();

        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("entry")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private static CnBlogNewsItemInfo readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "entry");
        String id = null;
        String title = null;
        String summary = null;
        String published = null;
        String topicIcon = null;
        String sourceName = null;
        String link = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("sourceName")) {
                sourceName = readSourceName(parser);
            } else if (name.equals("topicIcon")) {
                topicIcon = readTopicIcon(parser);
            } else if (name.equals("published")) {
                published = readPublished(parser);
            } else if (name.equals("id")) {
                id = readId(parser);
            } else if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("summary")) {
                summary = readSummary(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else {
                skip(parser);
            }
        }
        return new CnBlogNewsItemInfo(id, title, summary, link, published, topicIcon, sourceName);
    }

    private static String readSourceName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "sourceName");
        String sourceName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "sourceName");
        return sourceName;
    }

    private static String readTopicIcon(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "topicIcon");
        String topicIcon = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "topicIcon");
        return topicIcon;
    }

    private static String readPublished(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "published");
        String published = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "published");
        return published;
    }

    private static String readId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "id");
        String id = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "id");
        return id;
    }

    // Processes title tags in the feed.
    public static String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes link tags in the feed.
    public static String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")) {
                link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // Processes summary tags in the feed.
    public static String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "summary");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "summary");
        return summary;
    }

    // For the tags title and summary, extracts their text values.
    public static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    public static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
