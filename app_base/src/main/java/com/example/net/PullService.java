package com.example.net;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import java.io.InputStream;

/**
 * Created by leigod on 2018/5/30.
 */

public class PullService {
    public static String readXml(InputStream inStream) throws Exception {
        String url = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inStream, "UTF-8");
        //得到PULL解析器的事件，其返回值是int类型的
        int eventCode = parser.getEventType();
        // 用switch循环，如果获得的事件码是文档结束，那么结束解析（parser.next()来触发下一事件）
        while (eventCode != XmlPullParser.END_DOCUMENT) {
            switch (eventCode) {
                case XmlPullParser.START_DOCUMENT:// 文档开始事件
                    break;
                case XmlPullParser.START_TAG:// 开始元素
                    // 判断当前元素是否是需要检索的元素
                    if ("script".equals(parser.getName())){
                        url=parser.nextText();
                    }
//                    if ("person".equals(parser.getName())) {
//                        person.setId(Integer.parseInt(parser.getAttributeValue(0)));
//                    } else if (person != null) {
//                        if ("name".equals(parser.getName())) {
//                        } else if ("age".equals(parser.getName())) {
//                        }
//                    }
                    break;
                case XmlPullParser.END_TAG:// 结束元素

                    break;
                default:
                    break;
            }
            //下一个事件，是parser中最重要的方法
            eventCode = parser.next();
        }
        inStream.close();
//        Logger.i("pull--->",url+"\n");
        return url;
    }


}
