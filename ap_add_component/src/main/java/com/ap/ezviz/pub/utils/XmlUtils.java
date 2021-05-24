package com.ap.ezviz.pub.utils;

import android.text.TextUtils;
import android.util.Xml;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

/**
 * xml辅助工具类
 * Created by youguanglai on 2018/1/29.
 */
public class XmlUtils {

    private static Serializer serializer;

    private static Serializer getSerializer() {
        synchronized (XmlUtils.class) {
            if (serializer == null) {
                serializer = new Persister(new AnnotationStrategy());
            }
        }
        return serializer;
    }

    public static String toXml(Object object) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        getSerializer().write(object,baos);
        return baos.toString();
    }

    public static <T> T formXml(String xml, Class<T> clazz) throws Exception {
        return getSerializer().read(clazz, xml);
    }

    public static String getStringValue(String xml, String element) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(xml));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (element.equals(parser.getName())) {
                            int count = parser.getAttributeCount();
                            if (count == 0) {
                                return parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static String getAttrValue(String xml, String element, String attr) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(xml));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (element.equals(parser.getName())) {
                            String attrValue = parser.getAttributeValue(null, attr);
                            if (!TextUtils.isEmpty(attrValue)){
                                return parser.getAttributeValue(null, attr);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
