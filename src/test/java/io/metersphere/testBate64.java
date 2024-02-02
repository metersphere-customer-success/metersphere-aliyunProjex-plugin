package io.metersphere;

import org.junit.Test;

import java.util.Base64;

public class testBate64 {
    @Test
    public void ttt() throws Exception {
        String a = "PUT\n" +
                "\n" +
                "\n" +
                "Sat, 02 Sep 2023 08:34:48 GMT\n" +
                "/testcjg/bb54139a.png";
        String AccessKeySecret = "w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF";
        String s = sign(AccessKeySecret, a);
        System.out.println(s);
        System.out.println("lA2f2Mt++yU1vukcqmLL/uAmBx8=");
        System.out.println("lA2f2Mt++yU1vukcqmLL/uAmBx8=");
    }

    public String sign(String accessSecret, String stringToSign) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        mac.init(new javax.crypto.spec.SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(signData);
    }

}
