package io.metersphere.platform.client;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

public class SignUtil {
    static final java.text.SimpleDateFormat DF = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    static String accessKeyId;
    static String accessSecret;

    SignUtil(String ak, String sk){
        this.accessKeyId = ak;
        this.accessSecret = sk;
    }
    public String getSignature(String action) throws Exception {
        //声明个排序map接受参数
        TreeMap<String,String> paras = new TreeMap<String, String>();
        paras.put("AccessKeyId",accessKeyId);
        paras.put("Action",action);
        paras.put("Format","XML");
        paras.put("SignatureNonce",java.util.UUID.randomUUID().toString());
//        paras.put("SignatureNonce","3ee8c1b8-83d3-44af-a94f-4e0ad82fd6cf");
        paras.put("SignatureVersion","1.0");
        paras.put("Timestamp", DF.format(new java.util.Date()));
//        paras.put("Timestamp", "2016-02-23T12:46:24Z");
        paras.put("Version","2021-06-25");
        paras.put("SignatureMethod","HMAC-SHA1");

        //拼接请求参数
        StringBuilder keys = new StringBuilder();
        java.util.Iterator<String> it = paras.keySet().iterator();
        while (it.hasNext()){
            String key = it.next();
            keys = keys.append("&").append(key).append("=").append(specialUrlEncode(paras.get(key)));
        }
        System.out.println(keys);
        //拼接完要将第一个&去掉
        String sign =keys.substring(1);

        //拼接请求方法
        StringBuilder signAll = new StringBuilder();
        signAll.append("GET").append("&").append(specialUrlEncode("/")).append("&");
        //构造完整的请求字符串
        signAll.append(specialUrlEncode(sign));
        //把 accessKeySecret加上"&"构成 HMAC-SHA1 算法的key
        String accessSecretKey = accessSecret+"&";
        //计算签名值
        String signature = getSignature(signAll.toString(),accessSecretKey);
        return signature;
    }
    //
    public String specialUrlEncode(String value) throws Exception {
        return java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }
    public String getSignature(String stringTosign, String accessSecretKey) throws NoSuchAlgorithmException {
        byte[] result = null;
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(accessSecretKey.getBytes(), "HmacSHA1");
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance("HmacSHA1");
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac = mac.doFinal(stringTosign.getBytes());
            result = Base64.encodeBase64(rawHmac);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (null != result) {
            return new String(result);
        } else {
            return null;
        }
    }

}