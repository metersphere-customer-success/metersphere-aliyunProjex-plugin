package io.metersphere.platform.client;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

public class Sender {
    /*
     * 计算MD5+BASE64
     */
    public static String MD5Base64(String s) {
        if (s == null)
            return null;
        String encodeStr = "";
        byte[] utfBytes = s.getBytes();
        MessageDigest mdTemp;
        try {
            mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(utfBytes);
            byte[] md5Bytes = mdTemp.digest();
            encodeStr = new String(Base64.encodeBase64(md5Bytes));
//            result = ;
//            Base64Encoder b64Encoder = new Base64Encoder();
//            encodeStr = b64Encoder.encode(md5Bytes);
        } catch (Exception e) {
            throw new Error("Failed to generate MD5 : " + e.getMessage());
        }
        return encodeStr;
    }
    /*
     * 计算 HMAC-SHA1
     */
    public static String HMACSha1(String data, String key) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = new String(Base64.encodeBase64(rawHmac));
//            encodeStr = new String(Base64.encodeBase64(rawHmac));
//
//            (new BASE64Encoder()).encode(rawHmac);
        } catch (Exception e) {
            throw new Error("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }
    /*
     * 获取时间
     */
    public static String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }


    /*
     * 发送POST请求
     */
    public static String sendPost(String url, String body, String ak_id, String ak_secret) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "POST";
            String accept = "application/json";
            String content_type = "application/json;chrset=utf-8";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            String host = realUrl.getHost();
            // 1.对body做MD5+BASE64加密
            String bodyMd5 = MD5Base64(body);
            String uuid = UUID.randomUUID().toString();
            String stringToSign = method + "\n" + accept + "\n" + bodyMd5 + "\n" + content_type + "\n" + date + "\n"
                    + "x-acs-signature-method:HMAC-SHA1\n"
                    + "x-acs-signature-nonce:" + uuid + "\n"
                    + "x-acs-version:2019-01-02\n"
                    + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "acs " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("Accept", accept);
            conn.setRequestProperty("Content-Type", content_type);
            conn.setRequestProperty("Content-MD5", bodyMd5);
            conn.setRequestProperty("Date", date);
            conn.setRequestProperty("Host", host);
            conn.setRequestProperty("Authorization", authHeader);
            conn.setRequestProperty("x-acs-signature-nonce", uuid);
            conn.setRequestProperty("x-acs-signature-method", "HMAC-SHA1");
            conn.setRequestProperty("x-acs-version", "2019-01-02");  // 版本可选
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(body);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            InputStream is;
            HttpURLConnection httpconn = (HttpURLConnection) conn;
            if (httpconn.getResponseCode() == 200) {
                is = httpconn.getInputStream();
            } else {
                is = httpconn.getErrorStream();
            }
            in = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    public static HttpHeaders sendGet(String url, String ak_id, String ak_secret, String action) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "GET";
            String accept = "application/json";
            String content_type = "application/json;chrset=utf-8";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            String host = realUrl.getHost();
            // 1.对body做MD5+BASE64加密
            String uuid = UUID.randomUUID().toString();
            String stringToSign = method + "\n" + accept + "\n" + content_type + "\n" + date + "\n"
                    + "x-acs-signature-method:HMAC-SHA1\n"
                    + "x-acs-signature-nonce:" + uuid + "\n"
                    + "x-acs-version:2021-06-25\n"
                    + "Action:" + "listProjects" + "\n"
                    + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            ak_id = "TMP.3Ke5LNWn1hhhU4BxL3ewELpBN2N8Yk12JB3CVguymwEYbBdaFswFHLsZn4APEVJyjbiafxuVoaHuVCHiwZQMqPdDejpBKg";
            // 3.得到 authorization header
            String authHeader = "acs " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            HttpHeaders conn = new HttpHeaders();
            // 设置通用的请求属性
            conn.add("x-acs-action", action);
            conn.add("x-acs-version", "2021-06-25");  // 版本可选
            conn.add("Accept", accept);
            conn.add("Authorization", authHeader);
            String  a = "acs TMP.3Ke5LNWn1hhhU4BxL3ewELpBN2N8Yk12JB3CVguymwEYbBdaFswFHLsZn4APEVJyjbiafxuVoaHuVCHiwZQMqPdDejpBKg:NKC9U72IXzGp5rxR8M0rhdHY9wY=";
            System.out.println(a);
            System.out.println(authHeader);
            conn.add("x-acs-signature-nonce", uuid);
            conn.add("Date", date);
            conn.add("x-acs-signature-method", "HMAC-SHA1");
            conn.add("Content-Type", content_type);
            conn.add("Host", host);
            return conn;
        }catch (Exception e){}
        return null;
    }
//
//            // 发送POST请求必须设置如下两行
//            conn.setDoOutput(true);
////            conn.setDoInput(true);
//            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
//            // 发送请求参数
//            // flush输出流的缓冲
//            out.flush();
//            // 定义BufferedReader输入流来读取URL的响应
//            InputStream is;
//            HttpURLConnection httpconn = (HttpURLConnection) conn;
//            if (httpconn.getResponseCode() == 200) {
//                is = httpconn.getInputStream();
//            } else {
//                is = httpconn.getErrorStream();
//            }
//            in = new BufferedReader(new InputStreamReader(is));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//        } catch (Exception e) {
//            System.out.println("发送 POST 请求出现异常！" + e);
//            e.printStackTrace();
//        }
//        // 使用finally块来关闭输出流、输入流
//        finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return result;

}