package ru.startandroid.journalofhealth;

import android.content.Context;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Xml {

    public static String filename = "load_results.xml";

    public static void loadXML(final Context context) {
        Log.d(MainActivity.LOG_TAG, "load");
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {

                boolean data = false;
                boolean high = false;
                boolean low = false;
                boolean pulse = false;
                boolean sugar = false;
                boolean comment = false;
                String tdata = "";
                String thigh = "";
                String tlow = "";
                String tpulse = "";
                String tsugar = "";
                String tcomment = "";

                public void startElement(String uri, String localName,
                                         String qName, Attributes attributes)
                        throws SAXException {
                    if (qName.equalsIgnoreCase("data")) {
                        data = true;
                    }
                    if (qName.equalsIgnoreCase("high")) {
                        high = true;
                    }
                    if (qName.equalsIgnoreCase("low")) {
                        low = true;
                    }
                    if (qName.equalsIgnoreCase("pulse")) {
                        pulse = true;
                    }
                    if (qName.equalsIgnoreCase("sugar")) {
                        sugar = true;
                    }
                    if (qName.equalsIgnoreCase("comment")) {
                        comment = true;
                    }
                }

                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {
                    if (qName.equalsIgnoreCase("result")) {
                        DBHelper dbHelper = new DBHelper(context);
                        dbHelper.add(tdata, thigh, tlow, tpulse, tsugar, tcomment);
                        tdata = "";
                        thigh = "";
                        tlow = "";
                        tpulse = "";
                        tsugar = "";
                        tcomment = "";
                    }
                }

                public void characters(char ch[], int start, int length)
                        throws SAXException {

                    if (data) {
                        tdata = new String(ch, start, length);
                        data = false;
                    }
                    if (high) {
                        thigh = new String(ch, start, length);
                        high = false;
                    }
                    if (low) {
                        tlow = new String(ch, start, length);
                        low = false;
                    }
                    if (pulse) {
                        tpulse = new String(ch, start, length);
                        pulse = false;
                    }
                    if (sugar) {
                        tsugar = new String(ch, start, length);
                        sugar = false;
                    }
                    if (comment) {
                        tcomment = new String(ch, start, length);
                        comment = false;
                    }
                }
            };

            InputStream is = context.getAssets().open(filename);
            saxParser.parse(is, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
