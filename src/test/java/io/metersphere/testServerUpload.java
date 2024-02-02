//package io.metersphere;
//
//import java.util.Date;
//
//public class testServerUpload{
//    private String accessId = "LTAI5tJoCbDpuFjjZn54VE8U";
//    private String accessKey = "w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF";
//    private String endpoint = "projex";
//    private String bucket = "testcjg";
//
//    private OSS ossClient;
//    public JSONResult  ossSign(){
//        // host的格式为 bucketname.endpoint
//        String host = "https://" + bucket + "." + endpoint;
//
//        // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
////        String callbackUrl = "http://localhost:1040/callback";
//
//        // 用户上传文件时指定的前缀。
//        String dir = "itsource";
//
//        try {
//            long expireTime = 30;
//            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
//            Date expiration = new Date(expireEndTime);
//
//            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
////            PolicyConditions policyConds = new PolicyConditions();
////            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
////            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
//
//            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
//            byte[] binaryData = postPolicy.getBytes("utf-8");
//            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
//            String postSignature = ossClient.calculatePostSignature(postPolicy);
//
//            Map<String, String> respMap = new LinkedHashMap<String, String>();
//            respMap.put("accessid", accessId);
//            respMap.put("policy", encodedPolicy);
//            respMap.put("signature", postSignature);
//            respMap.put("dir", dir);
//            respMap.put("host", host);
//            respMap.put("expire", String.valueOf(expireEndTime / 1000));
//
//            return JSONResult.ok().put("data",respMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//        } finally {
//            ossClient.shutdown();
//        }
//        return null;
//    }
//}