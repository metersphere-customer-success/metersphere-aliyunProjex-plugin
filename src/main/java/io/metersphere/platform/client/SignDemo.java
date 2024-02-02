package io.metersphere.platform.client;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;

public class SignDemo {


//        aliyunProjexConfig.setAccessKeyID("LTAI5tJoCbDpuFjjZn54VE8U");
//        aliyunProjexConfig.setAccessKeySecret("w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF");
//        aliyunProjexConfig.setOrganizationId("64ad2260702c0cacad9a1f2a");

    // HTTPMethod推荐使用POST
//    static final String API_HTTP_METHOD = "POST";
    static final String API_HTTP_METHOD = "GET";
    // API访问域名，与类目相关，具体类目的API访问域名请参考：https://help.aliyun.com/document_detail/143103.html
//    static final String API_ENDPOINT = "imageenhan.cn-shanghai.aliyuncs.com";
    static final String API_ENDPOINT = "devops.cn-hangzhou.aliyuncs.com";
    // API版本，与类目相关，具体类目的API版本请参考：https://help.aliyun.com/document_detail/464194.html
//    static final String API_VERSION = "2019-09-30";
    static final String API_VERSION = "2021-06-25";
    static final java.text.SimpleDateFormat DF = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//    public static void main(String[] args) throws Exception {
//        // 创建AccessKey ID和AccessKey Secret，请参见：https://help.aliyun.com/document_detail/175144.html
//        // 如果您使用的是RAM用户的AccessKey，还需要为子账号授予权限AliyunVIAPIFullAccess，请参见：https://help.aliyun.com/document_detail/145025.html
//        // 从环境变量读取配置的AccessKey ID和AccessKey Secret。运行代码示例前必须先配置环境变量。
//        String accessKeyId = "LTAI5tJoCbDpuFjjZn54VE8U";
//        String accessSecret = "w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF";
//        DF.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));// 这里一定要设置GMT时区
//        // 业务参数名字是大驼峰
//        Map<String, String> params = new HashMap<>();
//        // 注意，使用签名机制调用，文件参数目前仅支持上海OSS链接，可参考https://help.aliyun.com/document_detail/175142.html文档将文件放入上海OSS中。如果是其他情况（如本地文件或者其他链接），请先显式地转换成上海OSS链接，可参考https://help.aliyun.com/document_detail/155645.html文档中的方式二，但该方案不支持web前端环境直接调用。
////        params.put("Url", "http://viapi-demo.oss-cn-shanghai.aliyuncs.com/viapi-demo/images/MakeSuperResolution/sup-dog.png");
//        // API Action，能力名称，请参考具体算法文档详情页中的Action参数，这里以图像超分为例：https://help.aliyun.com/document_detail/151947.html
//        String action = "listProjects";
//        execute(action, accessKeyId, accessSecret, params);
//    }

    public static String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

//    public static void execute(String action, String accessKeyId, String accessSecret, Map<String, String> bizParams) throws Exception {
//        java.util.Map<String, String> params = new java.util.HashMap<String, String>();
//        // 1. 系统参数
//        params.put("SignatureMethod", "HMAC-SHA1");
//        params.put("SignatureNonce", java.util.UUID.randomUUID().toString());//防止重放攻击
//        params.put("AccessKeyId", accessKeyId);
//        params.put("SignatureVersion", "1.0");
//        params.put("Timestamp", DF.format(new java.util.Date()));
//        params.put("Format", "JSON");
//        params.put("Date", toGMTString(new Date()));
//        // 2. 业务API参数
////        params.put("RegionId", "cn-shanghai");
//        params.put("Version", API_VERSION);
//        params.put("Action", action);
//        if (bizParams != null && !bizParams.isEmpty()) {
//            params.putAll(bizParams);
//        }
//        // 3. 去除签名关键字Key
//        if (params.containsKey("Signature")) {
//            params.remove("Signature");
//        }
//        // 4. 参数KEY排序
//        java.util.TreeMap<String, String> sortParams = new java.util.TreeMap<String, String>();
//        sortParams.putAll(params);
//        // 5. 构造待签名的字符串
//        java.util.Iterator<String> it = sortParams.keySet().iterator();
//        StringBuilder sortQueryStringTmp = new StringBuilder();
//        while (it.hasNext()) {
//            String key = it.next();
//            sortQueryStringTmp.append("&").append(specialUrlEncode(key)).append("=").append(specialUrlEncode(params.get(key)));
//        }
//        String sortedQueryString = sortQueryStringTmp.substring(1);// 去除第一个多余的&符号
//        StringBuilder stringToSign = new StringBuilder();
//        stringToSign.append(API_HTTP_METHOD).append("&");
//        stringToSign.append(specialUrlEncode("/")).append("&");
//        stringToSign.append(specialUrlEncode(sortedQueryString));
//        String sign = sign(accessSecret + "&", stringToSign.toString());
//        // 6. 签名最后也要做特殊URL编码
//        String signature = specialUrlEncode(sign);
//        System.out.println(params.get("SignatureNonce"));
//        System.out.println("\r\n=========\r\n");
//        System.out.println(params.get("Timestamp"));
//        System.out.println("\r\n=========\r\n");
//        System.out.println(sortedQueryString);
//        System.out.println("\r\n=========\r\n");
//        System.out.println(stringToSign.toString());
//        System.out.println("\r\n=========\r\n");
//        System.out.println(sign);
//        System.out.println("\r\n=========\r\n");
//        System.out.println(signature);
//        System.out.println("\r\n=========\r\n");
//        // 最终生成出合法请求的URL
//        System.out.println("http://" + API_ENDPOINT + "/?Signature=" + signature + sortQueryStringTmp);
//        System.out.println("http://" + API_ENDPOINT + "/organization/64ad2260702c0cacad9a1f2a/listProjects");
//
//        // 添加直接做post请求的方法
//        try {
//            // 使用生成的 URL 创建POST请求
//            URIBuilder builder = new URIBuilder("http://" + API_ENDPOINT +"/organization/64ad2260702c0cacad9a1f2a/listProjects"+ "/?Signature=" + signature + sortQueryStringTmp);
//            URI uri = builder.build();
//            HttpGet request = new HttpGet(uri);
//            HttpClient httpclient = HttpClients.createDefault();
//            HttpResponse response = httpclient.execute(request);
//            HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                System.out.println(EntityUtils.toString(entity));
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
    public static String specialUrlEncode(String value) throws Exception {
        return java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }
    public static String sign(String accessSecret, String stringToSign) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        mac.init(new javax.crypto.spec.SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(signData);
    }
}