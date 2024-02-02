package io.metersphere.platform.client;


import io.metersphere.platform.api.BaseClient;
import io.metersphere.platform.domain.PlatformIssuesDTO;
import io.metersphere.platform.domain.SyncIssuesAttachmentRequest;
import io.metersphere.platform.domain.models.*;
import io.metersphere.platform.domain.oss.RequestMessage;
import io.metersphere.platform.domain.oss.SignUtils;
import io.metersphere.platform.impl.AliyunProjexPlatform;
import io.metersphere.plugin.exception.MSPluginException;
import io.metersphere.plugin.utils.JSON;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class AliyunProjexClient extends BaseClient {

    private String baseUrl = "https://devops.cn-hangzhou.aliyuncs.com";
    private AliyunProjexPlatform platform;

    public AliyunProjexClient(AliyunProjexPlatform platform){
        this.platform = platform;
    }
    private String getBaseUrl(){
        return baseUrl+platform.config.getOrganizationId()+"/";
    }

    private List<String> cookies = new ArrayList<>();

    String getCanonicalizedHeaders(LinkedHashMap<String, String> map){
        StringBuilder canonicalString = new StringBuilder();
        // Append canonicalized parameters
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            canonicalString.append(key).append(':').append(value);
            canonicalString.append("\n");
        }
        return canonicalString.toString();
    }

    public String createWorkitemV2(CreateWorkitemV2Request createWorkitemV2Request){
        ResponseEntity<String> response = null;
        String url = "/organization/"+platform.config.getOrganizationId()+"/workitem";
        try {
            HttpHeaders headers = getAuthHeader("POST",JSON.toJSONString(createWorkitemV2Request),"createWorkitemV2", url);
            HttpEntity<CreateWorkitemV2Request> requestEntity = new HttpEntity<>(createWorkitemV2Request,headers);
            response = restTemplate.exchange(baseUrl+url, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
        Map<String,String> map = JSON.parseMap(response.getBody());
        return map.get("workitemIdentifier");
    }

    public String specialUrlEncode(String value) throws Exception {
        return java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }
    public String sign(String accessSecret, String stringToSign) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        mac.init(new javax.crypto.spec.SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        return encoder.encodeToString(signData);
    }

    protected HttpHeaders getAuthHeader(String Method, String body, String action, String url) throws Exception {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("x-acs-action", action);
        map.put("x-acs-signature-method","HMAC-SHA1");
        map.put("x-acs-signature-nonce",UUID.randomUUID().toString());
        map.put("x-acs-version","2021-06-25");
        String CanonicalizedHeaders = getCanonicalizedHeaders(map);
        String CanonicalizedResource = url;
        String Accept = "application/json";
        String ContentType = "";
        String Date = getGMTDate();
        String ContentMD5 = "";
        if(StringUtils.isNotBlank(body)){
            ContentType = "application/json";
        }
        String stringToSign =
                Method + "\n" +
                        Accept + "\n" +
                        ContentMD5 + "\n" +
                        ContentType + "\n" +
                        Date + "\n" +
                        CanonicalizedHeaders +
                        CanonicalizedResource;
//        Map<String, String> mapTemp = new HashMap<>();
//        mapTemp.put("category", "Project");
        String signature = sign(platform.config.getAccessKeySecret(),stringToSign);
        HttpHeaders headers = new HttpHeaders();
        map.put("accept","application/json");
        map.put("date", Date);
        map.put("host","devops.cn-hangzhou.aliyuncs.com");
        for(Map.Entry<String, String> m : map.entrySet()){
            headers.add(m.getKey(), m.getValue());
        }
        String authorization = null;
        try{
            authorization = "acs " + platform.config.getAccessKeyID() + ":" + signature;
        }catch (Exception e){

        }
        headers.add("authorization", authorization);
        return headers;
    }
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private final static String CHARSET_UTF8 = "utf8";
    private final static String ALGORITHM = "HmacSHA1";
    public static String hmacSha1(String data, String key) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
            mac.init(keySpec);
            byte[] rawHmac;
            rawHmac = mac.doFinal(data.getBytes(CHARSET_UTF8));
            return new String(org.apache.commons.codec.binary.Base64.encodeBase64(rawHmac));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String buildSignData(String Date,String VERB,String CanonicalizedResource){
        String signData = VERB + "\n"+ "\n"+ "\n"
                + Date + "\n"
                + CanonicalizedResource;
        return signData;
    }
    protected HttpHeaders getOSSAuthHeader(String Method, String bucket, String key, String host, Object body, String action, String urdddl, String oss) throws Exception {

        String date = getOSSGMTDate();
        String ossBucket= "testcjg";
        String accessKeyId= "LTAI5tJoCbDpuFjjZn54VE8U";
        String secretAccessKey= "w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF";
        String resourcePath = "/testcjg/bb54139a.png";
        String resourcePath1 = "/bb54139a.png";
        String VERB = "PUT";
        String url = "http://"+ossBucket+".oss-cn-hangzhou.aliyuncs.com";

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("AccessKeyId","LTAI5tJoCbDpuFjjZn54VE8U");
        map.put("AccessKeySecret","w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF");
        map.put("Date", date);
        map.put("Host",ossBucket+".oss-cn-hangzhou.aliyuncs.com");

        String CanonicalizedOSSHeaders = getCanonicalizedHeaders(map);

        String ContentMD5 = "" ;
        String ContentType = "image/png" ;
        String stringToSign = VERB + "\n"
                + ContentMD5 + "\n"
                + ContentType + "\n"
                + date + "\n"
//                + CanonicalizedOSSHeaders
                + resourcePath;
//        String Signature = (hmacSha1(buildSignData(date,VERB,resourcePath),secretAccessKey));
//        String Signature = base64(hmac-sha1(AccessKeySecret,
//                VERB + "\n"
//                        + Content-MD5 + "\n"
//                        + Content-Type + "\n"
//                        + Date + "\n"
//                        + CanonicalizedOSSHeaders
//                        + CanonicalizedResource))

        String Signature = sign(secretAccessKey,stringToSign);

        String Authorization="OSS " + accessKeyId + ":" + Signature;
        RequestMessage request = new RequestMessage();
        request.setMethod(io.metersphere.platform.domain.oss.HttpMethod.PUT);
        request.addHeader("data",date);
        request.addHeader("ContentType",ContentType);
        request.addHeader("ContentMD5",ContentMD5);
        request.addHeader("bucket",ossBucket);
//        request.addHeader();
        String signature = SignUtils.buildSignature(secretAccessKey, "PUT", resourcePath, request);

//        String Authorizationww="OSS " + accessKeyId + ":" + Signaturww;


        System.out.println(Authorization);
        HttpHeaders headers = new HttpHeaders();
//        headers.add("AccessKeyId","LTAI5tJoCbDpuFjjZn54VE8U");
//        headers.add("AccessKeySecret","w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF");
        headers.add("Authorization", Authorization);
//        headers.add("Transfer-Encoding","chunked");

//        String date = getGMTDate();
//        String ossBucket= "testcjg";
//        String accessKeyId= "LTAI5tJoCbDpuFjjZn54VE8U";
//        String secretAccessKey= "w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF";
//        String resourcePath = "testcjg/bb54139a.png";
//        String resourcePath1 = "/bb54139a.png";
//        String VERB = "PUT";
//        String url = "http://testcjg.oss-cn-hangzhou.aliyuncs.com";
        headers.add("Date",date);
//        headers.add("Content-Type","image/png");
        headers.add("Host",ossBucket+".oss-cn-hangzhou.aliyuncs.com");

        return headers;

//        Map<String, String> headersTemp = new LinkedHashMap<>();
//
////        headersTemp.put("AccessKeyId","LTAI5tJoCbDpuFjjZn54VE8U");
////        headersTemp.put("AccessKeySecret","w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF");
//
//        headersTemp.put("Date",date);
//        headersTemp.put("Host",ossBucket+".oss-cn-hangzhou.aliyuncs.com");
//
//        RequestMessage ttt = new RequestMessage(ossBucket, resourcePath1);
//        ttt.setHeaders(headersTemp);
//        ttt.setMethod(io.metersphere.platform.domain.oss.HttpMethod.PUT);
////        ttt.setEndpoint();
//        ttt.setKey(resourcePath1);
////        ttt.set
//        String signature = SignUtils.buildSignature(secretAccessKey, "PUT", resourcePath, ttt);
////        ttt.addHeader(OSSHeaders.AUTHORIZATION, SignV2Utils.composeRequestAuthorization(accessKeyId,signature, ttt));



//        headers.add(OSSHeaders.AUTHORIZATION, SignUtils.composeRequestAuthorization(accessKeyId, signature));


//        URL url1 = new URL(url + resourcePath1);
//        HttpURLConnection connection = null;
//        StringBuffer sbuffer=null;
//        try {
//            connection = (HttpURLConnection) url1.openConnection();
//            connection.setDoOutput(true);// HTTP正文内，因此需要设为true，默认情况下是false。
//            connection.setRequestMethod(HttpMethod.PUT.name()); // 可以根据需要 提交 GET、POST、DELETE、PUT等HTTP提供的功能。
//            connection.setRequestProperty("Date", date);  //设置请求的服务器网址和域名，例如***.**.***.***。
//            connection.setRequestProperty("Authorization", Authorization);//设定请求Json格式，也可以设定XML格式的。
//            connection.setReadTimeout(10000);//设置读取超时时间。
//            connection.setConnectTimeout(10000);//设置连接超时时间。
//            connection.connect();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        for(){
//
//        };
//        return headers;

//                LinkedHashMap<String, String> map = new LinkedHashMap<>();
////        map.put("bucket", bucket);
//        map.put("key", key);
////        map.put("User-Agent", "AlibabaCloud API Workbench");
//        map.put("x-acs-action", action);
//        map.put("x-acs-signature-method","HMAC-SHA1");
//        map.put("x-acs-signature-nonce",UUID.randomUUID().toString());
//        map.put("x-acs-version","2019-05-17");
//        String CanonicalizedHeaders = getCanonicalizedHeaders(map);
//        String CanonicalizedResource = url;
//        String HTTPMethod = Method;
////        String Accept = "application/json";
//        String ContentType = "";
//        String Date = getGMTDate();
//        String ContentMD5 = "";
////        if(StringUtils.isNotBlank(body+"")){
////            ContentType = "application/json";
////        }
//        String stringToSign =
//                HTTPMethod + "\n" +
////                        Accept + "\n" +
//                        ContentMD5 + "\n" +
//                        ContentType + "\n" +
//                        Date + "\n" +
//                        CanonicalizedHeaders +
//                        CanonicalizedResource;
////        Map<String, String> mapTemp = new HashMap<>();
////        mapTemp.put("category", "Project");
//        String signature = sign(platform.config.getAccessKeySecret(),stringToSign);
//        HttpHeaders headers = new HttpHeaders();
////        map.put("accept","application/json");
////        map.put("Content-Type","image/png");
//        map.put("date", Date);
//        map.put("host",host);
//        for(Map.Entry<String, String> m : map.entrySet()){
//            headers.add(m.getKey(), m.getValue());
//        }
//        String authorization = null;
//        try{
//            authorization = oss + platform.config.getAccessKeyID() + ":" + signature;
//        }catch (Exception e){
//
//        }
//        headers.add("Authorization", authorization);
//        return headers;
    }

    protected HttpHeaders getAuthHeader(String action, String url) throws Exception {
        return getAuthHeader("GET",null, action, url);
    }

    String getGMTDate(){
        Date date = new Date();
        DateFormat gmt = new SimpleDateFormat("EEE, d-MMM-yyyy HH:mm:ss z", Locale.ENGLISH);
        gmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateStr = gmt.format(date);
        return dateStr;
    }

    public String getOSSGMTDate(){
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String str = sdf.format(cd.getTime());
        SimpleDateFormat ttt = new SimpleDateFormat("yyyy HH:mm:ss");
        System.out.println(ttt.format(cd.getTime()));
        return str;
    }


    public List<ListProjectsResponseBody.Projects> getProjectList(){
        ResponseEntity<String> response = null;
        String url = "/organization/"+platform.config.getOrganizationId()+"/listProjects?category=Project&maxResults=200&organizationId="+platform.config.getOrganizationId();
        try {
            HttpHeaders headers = getAuthHeader("listProjects", url);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            response = restTemplate.exchange(baseUrl+url, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
        ListProjectsResponseBody body = JSON.parseObject(response.getBody(), ListProjectsResponseBody.class);
        return body.getProjects();
    }


    public List<ListProjectWorkitemTypesResponseBody.WorkitemTypes> listProjectWorkitemTypes(String type){
        ResponseEntity<String> response = null;
        String url = "/organization/"+platform.config.getOrganizationId()+"/projects/"+platform.aliyunProjexProjectConfig.getProjectIdentifier()+"/getWorkitemType" +
                "?category=" +type+
                "&organizationId=" + platform.config.getOrganizationId() +
                "&projectId=" + platform.aliyunProjexProjectConfig.getProjectIdentifier() +
                "&spaceType=Project";
        try {
            HttpHeaders headers = getAuthHeader("getWorkitemType", url);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            response = restTemplate.exchange(baseUrl+url, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
        ListProjectWorkitemTypesResponseBody body = JSON.parseObject(response.getBody(), ListProjectWorkitemTypesResponseBody.class);
        return body.getWorkitemTypes();
    }

    public GetWorkItemInfoResponseBody.Workitem getWorkItemInfo(String workItemId){
        ResponseEntity<String> response = null;
        String url = "/organization/"+platform.config.getOrganizationId()+"/workitems/"+workItemId+
                "?organizationId=" + platform.config.getOrganizationId() +
                "&workitemId="+workItemId;
        try {
            HttpHeaders headers = getAuthHeader("getWorkItemInfo", url);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            response = restTemplate.exchange(baseUrl+url, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
        cookies = response.getHeaders().get("Set-Cookie");
        GetWorkItemInfoResponseBody body = JSON.parseObject(response.getBody(), GetWorkItemInfoResponseBody.class);
        return body.getWorkitem();
//        GetWorkItemInfoResponseBody
    }

    public List<ListWorkitemsResponseBody.Workitems> listWorkitems(String type) {
        List<ListWorkitemsResponseBody.Workitems> temp = new ArrayList<>();
        String nextToken = null;
        int maxResults = 200;
        int size = 0;
        do {
            ResponseEntity<String> response = null;
            String url = "/organization/" + platform.config.getOrganizationId() + "/listWorkitems" +
                    "?category=" + type +
                    "&maxResults=" + maxResults;
            if (nextToken != null) url += "&nextToken="+nextToken;
            url += "&organizationId=" + platform.config.getOrganizationId() +
                    "&spaceIdentifier=" + platform.aliyunProjexProjectConfig.getProjectIdentifier() +
                    "&spaceType=Project";
            try {
                HttpHeaders headers = getAuthHeader("listWorkitems", url);
                HttpEntity<String> requestEntity = new HttpEntity<>(headers);
                response = restTemplate.exchange(baseUrl + url, HttpMethod.GET, requestEntity, String.class);
            } catch (Exception e) {
                MSPluginException.throwException(e.getMessage());
            }
            ListWorkitemsResponseBody body = JSON.parseObject(response.getBody(), ListWorkitemsResponseBody.class);
            if(body.getWorkitems() != null) temp.addAll(body.getWorkitems());
            if(StringUtils.isEmpty(body.getNextToken())) return temp;
            nextToken = body.getNextToken();
            size = body.getWorkitems().size();
        } while (size == maxResults);
        return temp;
    }


    public ListWorkItemAllFieldsResponseBody listWorkItemAllFields() {
        ResponseEntity<String> response = null;
        String url = "/organization/" + platform.config.getOrganizationId()+"/workitems/fields/listAll" +
                "?organizationId=" + platform.config.getOrganizationId() +
                "&spaceIdentifier=" + platform.aliyunProjexProjectConfig.getProjectIdentifier() +
                "&spaceType=Project" +
                "&workitemTypeIdentifier=" + platform.aliyunProjexProjectConfig.getAliyunIssueTypeId();
        try {
            HttpHeaders headers = getAuthHeader("listWorkItemAllFields", url);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            response = restTemplate.exchange(baseUrl+url, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
        ListWorkItemAllFieldsResponseBody body = JSON.parseObject(response.getBody(), ListWorkItemAllFieldsResponseBody.class);
        return body;
    }


    public List<ListWorkItemWorkFlowStatusResponseBody.Statuses> listWorkItemWorkFlowStatus(){
        ResponseEntity<String> response = null;
        String url = "/organization/"+platform.config.getOrganizationId()+"/workitems/workflow/listWorkflowStatus"+
                "?organizationId="+platform.config.getOrganizationId() +
                "&spaceIdentifier="+platform.aliyunProjexProjectConfig.getProjectIdentifier() +
                "&spaceType=Project" +
                "&workitemCategoryIdentifier=Bug" +
                "&workitemTypeIdentifier="+platform.aliyunProjexProjectConfig.getAliyunIssueTypeId();
        try {
            HttpHeaders headers = getAuthHeader("listWorkItemWorkFlowStatus", url);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            response = restTemplate.exchange(baseUrl+url, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
        ListWorkItemWorkFlowStatusResponseBody body = JSON.parseObject(response.getBody(), ListWorkItemWorkFlowStatusResponseBody.class);
        return body.getStatuses();
    }

    public List<ListOrganizationMembersResponseBody.Members> listOrganizationMembers(){
        ResponseEntity<String> response = null;
        // 先搞200个不够再说
        String url = "/organization/"+platform.config.getOrganizationId()+"/members"+
                "?maxResults=200"+
                "&organizationId="+platform.config.getOrganizationId();
        try {
            HttpHeaders headers = getAuthHeader("listOrganizationMembers", url);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            response = restTemplate.exchange(baseUrl+url, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
        ListOrganizationMembersResponseBody body = JSON.parseObject(response.getBody(), ListOrganizationMembersResponseBody.class);
        return body.getMembers();
    }

    public void deleteWorkitem(String identifier){
        String url = "/organization/"+platform.config.getOrganizationId()+"/workitem/delete" +
                "?identifier="+identifier;
        try {
            HttpHeaders headers = getAuthHeader("DELETE",null,"deleteWorkitem", url);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            restTemplate.exchange(baseUrl+url, HttpMethod.DELETE, requestEntity, String.class);
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
    }

    public List<ListSprintsResponseBody.Sprints> listSprints(){
        ResponseEntity<String> response = null;
        String url = "/organization/"+platform.config.getOrganizationId()+"/sprints/list" +
                "?maxResults=200" +
                "&organizationId="+platform.config.getOrganizationId() +
                "&spaceIdentifier="+platform.aliyunProjexProjectConfig.getProjectIdentifier() +
                "&spaceType=Project";
        try {
            HttpHeaders headers = getAuthHeader("listSprints", url);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            response = restTemplate.exchange(baseUrl+url, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
        ListSprintsResponseBody body = JSON.parseObject(response.getBody(), ListSprintsResponseBody.class);
        return body.getSprints();
    }

    public GetWorkitemAttachmentCreatemetaResponseBody getWorkitemAttachmentCreatemeta(SyncIssuesAttachmentRequest request){
        ResponseEntity<String> response = null;
        String url = null;
        if(request.getSyncType().equals("upload")){
            url = "/organization/"+platform.config.getOrganizationId()+"/workitem/" +request.getPlatformId()+"/attachment/createmeta"+
                    "?fileName="+request.getFile().getName();
        }
        try {
            HttpHeaders headers = getAuthHeader("getWorkitemAttachmentCreatemeta", url);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            response = restTemplate.exchange(baseUrl+url, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
        GetWorkitemAttachmentCreatemetaResponseBody body = JSON.parseObject(response.getBody(), GetWorkitemAttachmentCreatemetaResponseBody.class);
        return body;
    }

    public void putObject(SyncIssuesAttachmentRequest request, GetWorkitemAttachmentCreatemetaResponseBody body) {
        ResponseEntity<String> response = null;
        HttpHeaders headers = null;
        try {
            headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap params= new LinkedMultiValueMap<>();
            params.add("OSSAccessKeyId",body.getUploadInfo().getAccessid());
            params.add("key",body.getUploadInfo().getDir());
            params.add("policy",body.getUploadInfo().getPolicy());
            params.add("success_action_status","200");
            params.add("signature",body.getUploadInfo().getSignature());
            params.add("file",request.getFile());
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
            response = restTemplate.exchange(body.getUploadInfo().getHost(), HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
        System.out.println(JSON.toJSONString(response.getBody()));
    }

    public void workitemAttachmentCreate(SyncIssuesAttachmentRequest request, GetWorkitemAttachmentCreatemetaResponseBody getWorkitemAttachmentCreatemetaResponseBody) {
        ResponseEntity<String> response = null;
        String url = "/organization/"+platform.config.getOrganizationId()+"/workitem/"+request.getPlatformId()+"/attachment";
        try {
            HttpHeaders headers = getAuthHeader("POST","dd","workitemAttachmentCreate", url);
            @Data
            class Temp {
                private String fileKey;
                private String originalFilename;
            }
            Temp temp = new Temp();
            temp.setFileKey(getWorkitemAttachmentCreatemetaResponseBody.getUploadInfo().getDir());
            temp.setOriginalFilename(request.getFile().getName());
            HttpEntity<Temp> requestEntity = new HttpEntity<>(temp,headers);
            response = restTemplate.exchange(baseUrl+url, HttpMethod.POST, requestEntity, String.class);
            System.out.println(response.getBody());
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
    }

    public Object listAttachment(PlatformIssuesDTO issuesWithBLOBs) {
        ResponseEntity<String> response = null;
        String url ="/projex/api/workitem/workitem/"+issuesWithBLOBs.getPlatformId()+"/attachment/list";
        try {
            HttpHeaders headers = getAuthHeader("listAttachment", url);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            response = restTemplate.exchange(baseUrl+url, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
        GetWorkitemAttachmentCreatemetaResponseBody body = JSON.parseObject(response.getBody(), GetWorkitemAttachmentCreatemetaResponseBody.class);
        return body;
    }

    public byte[] openFile(String filePath) {
        int HttpResult; // 服务器返回的状态
        byte[] bytes = null;
        try
        {
            URL url =new URL(filePath); // 创建URL
            URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码
            urlconn.connect();
            HttpURLConnection httpconn =(HttpURLConnection)urlconn;
            HttpResult = httpconn.getResponseCode();
            if(HttpResult != HttpURLConnection.HTTP_OK) {
                System.out.print("无法连接到");
            } else {
                int filesize = urlconn.getContentLength(); // 取数据长度
                System.out.println("取数据长度===="+filesize);
                urlconn.getInputStream();
                InputStream inputStream = urlconn.getInputStream();

                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                int ch;
                while ((ch = inputStream.read()) != -1) {
                    swapStream.write(ch);
                }
                bytes = swapStream.toByteArray();
            }
            System.out.println("文件大小===="+bytes.length);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  bytes;
    }

    public String getMDByHTML(String html) {
        ResponseEntity<String> response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            @Data
            class OptionsEntity{
                private String BulletListMarker= "-";
                private String CodeBlockStyle= "fenced";
                private String EmDelimiter= "_";
                private String EscapeMode= "smart";
                private String Fence= "```";
                private String HeadingStyle= "atx";
                private String HorizontalRule= "* * *";
                private String LinkStyle= "inlined";
                private String StrongDelimiter= "**";
            }
            @Data
            class Body {
                private String Content = html;
                private OptionsEntity Options = new OptionsEntity();
                private String Version = "V1";
            }
            Body b = new Body();
            HttpEntity<Body> requestEntity = new HttpEntity<>(b,headers);
            response = restTemplate.exchange("https://html-to-markdown.com/api/index", HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
       return response.getBody();
    }

    public void downloadImage(String path) {
//        int HttpResult; // 服务器返回的状态
//        byte[] bytes = null;
//        try
//        {
//            URL url = new URL(path); // 创建URL
//            URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码
//            urlconn.connect();
//            HttpURLConnection httpconn =(HttpURLConnection)urlconn;
//            HttpResult = httpconn.getResponseCode();
//            if(HttpResult != HttpURLConnection.HTTP_OK) {
//                System.out.print("无法连接到");
//            } else {
//                int filesize = urlconn.getContentLength(); // 取数据长度
//                System.out.println("取数据长度===="+filesize);
//                urlconn.getInputStream();
//                InputStream inputStream = urlconn.getInputStream();
//
//                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
//                int ch;
//                while ((ch = inputStream.read()) != -1) {
//                    swapStream.write(ch);
//                }
//                bytes = swapStream.toByteArray();
//            }
//            System.out.println("文件大小===="+bytes.length);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        ResponseEntity<String> response = null;
//        try {
//            HttpHeaders headers = getOSSAuthHeader();
//            HttpEntity requestEntity = new HttpEntity<>(headers);
//            List<HttpMessageConverter<?>> dd = restTemplate.getMessageConverters();
//            response = restTemplate.exchange(path, HttpMethod.GET, requestEntity, String.class);
//        } catch (Exception e) {
//            MSPluginException.throwException(e.getMessage());
//        }
    }

    public void updateWorkItem(UpdateWorkItemRequest updateWorkItemRequest) {
        ResponseEntity<String> response = null;
        String url = "/organization/"+platform.config.getOrganizationId()+"/workitems/update";
        try {
            HttpHeaders headers = getAuthHeader("POST","dd","updateWorkItem", url);
            HttpEntity<UpdateWorkItemRequest> requestEntity = new HttpEntity<>(updateWorkItemRequest,headers);
            response = restTemplate.exchange(baseUrl+url, HttpMethod.POST, requestEntity, String.class);
            System.out.println(response.getBody());
        } catch (Exception e) {
            MSPluginException.throwException(e.getMessage());
        }
    }
}
