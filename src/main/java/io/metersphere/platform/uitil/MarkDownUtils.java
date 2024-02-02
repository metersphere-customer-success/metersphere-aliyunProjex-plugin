package io.metersphere.platform.uitil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import io.metersphere.plugin.utils.JSON;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Map;

public class MarkDownUtils {
    public static void main(String[] args) throws IOException, ScriptException {
        String desc = "{\"htmlValue\":\"<article class=\\\"4ever-article\\\"><p style=\\\"text-align:left;text-indent:0;margin-left:0;margin-top:0;margin-bottom:0\\\"><span data-type=\\\"text\\\">测试desc</span></p><p style=\\\"text-align:left;text-indent:0;margin-left:0;margin-top:0;margin-bottom:0\\\"><span data-type=\\\"text\\\"></span><img src=\\\"https://devops.aliyun.com/projex/api/workitem/file/url?fileIdentifier=8c79ff7329141b8fd4f508cd29\\\" style=\\\"width:528px;height:83.656050955414px\\\"/><span data-type=\\\"text\\\"></span></p></article>\",\"jsonMLValue\":[\"root\",{},[\"p\",{},[\"span\",{\"data-type\":\"text\"},[\"span\",{\"data-type\":\"leaf\"},\"测试desc\"]]],[\"p\",{},[\"span\",{\"data-type\":\"text\"},[\"span\",{\"data-type\":\"leaf\"},\"\"]],[\"img\",{\"id\":\"822ady\",\"name\":\"飞书需求id.jpeg\",\"size\":101272,\"width\":528,\"height\":83.656050955414,\"src\":\"https://devops.aliyun.com/projex/api/workitem/file/url?fileIdentifier=8c79ff7329141b8fd4f508cd29\"},[\"span\",{\"data-type\":\"text\"},[\"span\",{\"data-type\":\"leaf\"},\"\"]]],[\"span\",{\"data-type\":\"text\"},[\"span\",{\"data-type\":\"leaf\"},\"\"]]]]}";

        Document doc = Jsoup.parse(desc);
        Elements headings = doc.select("*");
        for (Element heading : headings) {
            if(heading.tag().getName().equals("span")){
                String text = heading.text();
                System.out.println(text);
            }else if(heading.tag().getName().equals("img")){
                String stc = heading.attributes().get("src");
                stc = stc.substring(2,stc.length()-2);
                System.out.println(stc);

            }
        }

    }
}
