package io.metersphere;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.metersphere.platform.domain.*;
import io.metersphere.platform.impl.AliyunProjexPlatform;
import io.metersphere.plugin.utils.JSON;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegularTest {
    PlatformRequest platformRequest = new PlatformRequest();
    AliyunProjexPlatform aliyunProjexPlatform = null;
    AliyunProjexProjectConfig aliyunProjexProjectConfig = null;

    private String aliyunProjexProjectConfigStr(){
        return JSON.toJSONString(aliyunProjexProjectConfig);
    }

    @Before
    public void setConfig(){
        AliyunProjexConfig aliyunProjexConfig = new AliyunProjexConfig();
        aliyunProjexConfig.setAccessKeyID("LTAI5tFjnEKwhV8oW7LdqHBC");
        aliyunProjexConfig.setAccessKeySecret("vbZpjooLytNvGpVGllsMLtAGvddrMk");
        aliyunProjexConfig.setOrganizationId("5fcdbff6f9b3ccf7cd2cc5a6");
        platformRequest.setIntegrationConfig(JSON.toJSONString(aliyunProjexConfig));
//        AliyunProjexUserConfig aliyunProjexUserConfig = new AliyunProjexUserConfig();
//        aliyunProjexUserConfig.setAccessKeyID("LTAI5tJoCbDpuFjjZn54VE8U");
//        aliyunProjexUserConfig.setAccessKeySecret("w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF");
//        aliyunProjexUserConfig.setAccountId("1824892139824505");
//        platformRequest.setUserPlatformInfo(JSON.toJSONString(aliyunProjexUserConfig));
        aliyunProjexPlatform = new AliyunProjexPlatform(platformRequest);
        aliyunProjexProjectConfig = new AliyunProjexProjectConfig();
        aliyunProjexProjectConfig.setProjectIdentifier("7a5536c6a5cb15f506f1ab341d");
        aliyunProjexProjectConfig.setAliyunIssueTypeId("37da3a07df4d08aef2e3b393");
        aliyunProjexProjectConfig.setAliyunStoryTypeId("9uy29901re573f561d69jn40");

//        AliyunProjexConfig aliyunProjexConfig = new AliyunProjexConfig();
//        aliyunProjexConfig.setAccessKeyID("LTAI5tJoCbDpuFjjZn54VE8U");
//        aliyunProjexConfig.setAccessKeySecret("w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF");
//        aliyunProjexConfig.setOrganizationId("64ad2260702c0cacad9a1f2a");
//        platformRequest.setIntegrationConfig(JSON.toJSONString(aliyunProjexConfig));
//        AliyunProjexUserConfig aliyunProjexUserConfig = new AliyunProjexUserConfig();
//        aliyunProjexUserConfig.setAccessKeyID("LTAI5tJoCbDpuFjjZn54VE8U");
//        aliyunProjexUserConfig.setAccessKeySecret("w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF");
//        aliyunProjexUserConfig.setAccountId("1824892139824505");
//        platformRequest.setUserPlatformInfo(JSON.toJSONString(aliyunProjexUserConfig));
//        aliyunProjexPlatform = new AliyunProjexPlatform(platformRequest);
//        aliyunProjexProjectConfig = new AliyunProjexProjectConfig();
//        aliyunProjexProjectConfig.setProjectIdentifier("c475202f293922fdd8ddc551be");
//        aliyunProjexProjectConfig.setAliyunIssueTypeId("37da3a07df4d08aef2e3b393");
//        aliyunProjexProjectConfig.setAliyunStoryTypeId("9uy29901re573f561d69jn40");
    }

//    @Test
//    public void addd(){
//        String aa = null;
//        String ww = "";
//        System.out.println("wwww"+aa+ww);
//    }

    @Test
    public void asdads(){
        String html = "";
        String htmlTemp = "dasdasdasd";
        try{
            JSONObject json = JSONObject.parseObject(htmlTemp);
            html = json.getString("htmlValue");
        }catch (Exception e){
            System.out.println(htmlTemp);
        }
    }

    @Test
    public void tttt() throws IOException {
        String endpoint = "oss-cn-hangzhou.aliyuncs.com";
        String accessKeyId = "LTAI5tJoCbDpuFjjZn54VE8U";
        String accessKeySecret = "w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF";
        String bucketName = "testcjg";
        String path = "bb54139a.png";
        File file =new File("C:\\opt\\metersphere\\data\\image\\markdown\\bb54139a.png");
        SyncIssuesAttachmentRequest syncIssuesAttachmentRequest = new SyncIssuesAttachmentRequest();
        syncIssuesAttachmentRequest.setPlatformId("dddd");
        syncIssuesAttachmentRequest.setSyncType("upload");
        syncIssuesAttachmentRequest.setFile(file);
        aliyunProjexPlatform.syncIssuesAttachment(syncIssuesAttachmentRequest);
//        FileSystemResource fileResource = new FileSystemResource(file);
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//        PutObjectResult putObjectResult = ossClient.putObject(bucketName, path, fileResource.getInputStream());
//        putObjectResult.getETag();
    }

    @Test
    public void validateIntegrationConfig(){
        aliyunProjexPlatform.validateIntegrationConfig();
    }

    @Test
    public void validateProjectConfig(){
        aliyunProjexPlatform.validateProjectConfig(aliyunProjexProjectConfigStr());
    }

    @Test
    public void validateUserConfig(){
        aliyunProjexPlatform.validateUserConfig(JSON.toJSONString(platformRequest.getUserPlatformInfo()));
    }


    @Test
    public void getDemands() throws Exception {
        Object a = aliyunProjexPlatform.getDemands(aliyunProjexProjectConfigStr());
        p(a);
    }

    @Test
    public void loadDescImage() throws Exception {
        Object a = "[{\"id\":\"1e885b9e-81ff-4d00-9326-35e0297c5dbf\",\"name\":\"软件测试实验环境\",\"description\":null},{\"id\":\"9ce41d97-626d-4a88-a42f-4c2db7a5c32e\",\"name\":\"MeterSphere测试团队\",\"description\":\"MeterSphere测试团队\"},{\"id\":\"81c5c1ad-522d-42cd-9d81-fab526077c4a\",\"name\":\"演示\",\"description\":null},{\"id\":\"6d656251-25d5-4e87-aeca-3d3ef61cc449\",\"name\":\"北京信息科技大学\",\"description\":null},{\"id\":\"dd1dbf03-5bb2-4a44-9253-1d4180bbfb0d\",\"name\":\"苏州健雄\",\"description\":null},{\"id\":\"4825dd18-d9fb-4bb0-a246-8cb6c1e72977\",\"name\":\"广东环境保护\",\"description\":null},{\"id\":\"11b96e4a-1522-479e-a07c-f7df8735fbfe\",\"name\":\"大连理工\",\"description\":null},{\"id\":\"76ec3ae7-d86c-44e8-9589-1927f78a2274\",\"name\":\"广西农职大\",\"description\":null},{\"id\":\"ba9c1498-5690-11ed-a2b5-0242ac1e0a02\",\"name\":\"默认工作空间\",\"description\":\"系统默认创建的工作空间\"}]";
        JSONArray jsonArray = JSONArray.parseArray(a+"");
        jsonArray.size();
        System.out.println(jsonArray);
//        aliyunProjexPlatform.loadDescImage("d1\n" +
//                "![image.png](/resource/md/get?fileName=dfcb24a6.png)\n" +
//                "d2\n" +
//                "![image.png](/resource/md/get?fileName=da435fbe.png)");
//        aliyunProjexPlatform.testHttpImage("https://img0.baidu.com/it/u=1069797513,3670085775&fm=253&fmt=auto&app=120&f=PNG?w=1009&h=729");
//        File file = new File("https://img0.baidu.com/it/u=1069797513,3670085775&fm=253&fmt=auto&app=120&f=PNG?w=1009&h=729");
//        byte[] yte = openFile("https://img0.baidu.com/it/u=1069797513,3670085775&fm=253&fmt=auto&app=120&f=PNG?w=1009&h=729");

//        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
//        String bba = encoder.encodeToString(yte);
//        System.out.println(bba);
    }

    @Test
    public void syncAllIssues(){
        SyncAllIssuesRequest syncAllIssuesRequest = new SyncAllIssuesRequest();
        syncAllIssuesRequest.setProjectConfig(aliyunProjexProjectConfigStr());
        aliyunProjexPlatform.syncAllIssues(syncAllIssuesRequest);
    }

    @Test
    public void test(){
        String bb = "{\"htmlValue\":\"<article class=\\\"4ever-article\\\"><p style=\\\"text-align:left;text-indent:0;margin-left:0\\\"><span>sdasd</span><img src=\\\"https://devops.aliyun.com/projex/api/workitem/file/url?fileIdentifier=b12d8fa1daa6b53ccabeb7a1a6\\\" style=\\\"width:350px;height:323px\\\"/><span> </span></p></article>\",\"jsonMLValue\":[\"root\",{},[\"p\",{},[\"span\",{\"data-type\":\"text\"},[\"span\",{\"data-type\":\"leaf\"},\"sdasd\"]],[\"img\",{\"id\":\"r4y9da\",\"name\":\"image.png\",\"size\":4259,\"width\":350,\"height\":323,\"rotation\":0,\"src\":\"https://devops.aliyun.com/projex/api/workitem/file/url?fileIdentifier=b12d8fa1daa6b53ccabeb7a1a6\"},[\"span\",{\"data-type\":\"text\"},[\"span\",{\"data-type\":\"leaf\"},\"\"]]],[\"span\",{\"data-type\":\"text\"},[\"span\",{\"data-type\":\"leaf\"},\" \"]]]]}";
        JSONObject json = JSONObject.parseObject(bb);
        String html = json.getString("htmlValue");
        System.out.println(html);
        String text = getMDByHtml(html);
        System.out.println(text);
    }

    private String getMDByHtml(String html){
        Document doc = Jsoup.parse(html);
        Elements headings = doc.select("*");
        String htmlTemp = "";
        for (Element heading : headings) {
            if(StringUtils.equalsAny(heading.tag().getName(), "span", "body")){
                String text = heading.text();
                htmlTemp += text+"\\n";
                System.out.println(heading.className()+"   "+text);
            }
            if(heading.tag().getName().equals("img")){
                String stc = heading.attributes().get("src");
                stc = stc.substring(2,stc.length()-2);
                htmlTemp += stc+"\\n";
                System.out.println(heading.className()+"   "+stc);
            }
        }
        return htmlTemp;
    }

    protected String htmlDesc2MsDesc(String htmlDesc) {
        String desc = this.htmlImg2MsImg(htmlDesc);
        Document document = Jsoup.parse(desc);
        document.outputSettings((new Document.OutputSettings()).prettyPrint(false));
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");
        desc = document.html().replaceAll("\\\\n", "\n");
        desc = Jsoup.clean(desc, "", Safelist.none(), (new Document.OutputSettings()).prettyPrint(false));
        return desc.replace("&nbsp;", "");
    }

    protected String htmlImg2MsImg(String input) {
        String regex = "(<img\\s*src=\\\"(.*?)\\\".*?>)";
        Pattern pattern = Pattern.compile(regex);
        if (StringUtils.isBlank(input)) {
            return "";
        } else {
            Matcher matcher = pattern.matcher(input);
            String result = input;

            while(matcher.find()) {
                String url = matcher.group(2);
                String path;
                String name;
                String mdLink;
                if (url.contains("/resource/md/get/")) {
                    path = url.substring(url.indexOf("/resource/md/get/"));
                    name = path.substring(path.indexOf("/resource/md/get/") + 26);
                    mdLink = "![" + name + "](" + path + ")";
                    result = matcher.replaceFirst(mdLink);
                    matcher = pattern.matcher(result);
                } else if (url.contains("/resource/md/get")) {
                    path = url.substring(url.indexOf("/resource/md/get"));
                    name = path.substring(path.indexOf("/resource/md/get") + 35);
                    mdLink = "![" + name + "](" + path + ")";
                    result = matcher.replaceFirst(mdLink);
                    matcher = pattern.matcher(result);
                }
            }

            return result;
        }
    }

    @Test
    public void SyncIssuesAttachmentRequest(){
        SyncIssuesAttachmentRequest test = new SyncIssuesAttachmentRequest();
        test.setPlatformId("b83f159cc0c2416b8579e7db4f");
    }

    private void p(Object... o){
        for(int i = 0 ; i < o.length; i ++){
            System.out.println(JSON.toJSONString(o[i]));
        }
    }


    @Test
    public void getIssueTypes(){
        GetOptionRequest getOptionRequest = new GetOptionRequest();
        getOptionRequest.setProjectConfig(aliyunProjexProjectConfigStr());
        aliyunProjexPlatform.getIssueTypes(getOptionRequest);
    }

    @Test
    public void getStoryTypes(){
        GetOptionRequest getOptionRequest = new GetOptionRequest();
        getOptionRequest.setProjectConfig(aliyunProjexProjectConfigStr());
        aliyunProjexPlatform.getStoryTypes(getOptionRequest);
    }

    @Test
    public void getThirdPartCustomField(){
        p(aliyunProjexPlatform.getThirdPartCustomField(aliyunProjexProjectConfigStr()));
    }
}
