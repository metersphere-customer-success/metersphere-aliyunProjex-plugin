//package io.metersphere;
//
////import com.aliyun.devops20210625.models.ListProjectsRequest;
////import com.aliyun.devops20210625.models.ListProjectsResponse;
////import com.aliyun.tea.TeaException;
////import com.aliyun.auth.credentials.Credential;
////import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
////import com.aliyun.sdk.service.devops20210625.AsyncClient;
////import com.aliyun.sdk.service.devops20210625.models.CreateWorkitemV2Request;
////import com.aliyun.sdk.service.devops20210625.models.CreateWorkitemV2Response;
////import darabonba.core.client.ClientOverrideConfiguration;
////import io.metersphere.plugin.utils.JSON;
////import org.junit.Test;
//
//// This file is auto-generated, don't edit it. Thanks.
//
////import com.aliyun.auth.credentials.Credential;
////import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
////import com.aliyun.core.http.HttpClient;
////import com.aliyun.core.http.HttpMethod;
////import com.aliyun.core.http.ProxyOptions;
////import com.aliyun.httpcomponent.httpclient.ApacheAsyncHttpClientBuilder;
////import com.aliyun.sdk.service.devops20210625.models.*;
////import com.aliyun.sdk.service.devops20210625.*;
////import darabonba.core.RequestConfiguration;
////import darabonba.core.client.ClientOverrideConfiguration;
////import darabonba.core.utils.CommonUtil;
////import darabonba.core.TeaPair;
//import com.aliyun.oss.OSS;
//import com.aliyun.oss.OSSClientBuilder;
//import com.aliyun.oss.model.PutObjectResult;
//import org.junit.Test;
//import org.springframework.core.io.FileSystemResource;
//
//import java.awt.Insets;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.security.InvalidParameterException;
//
////import com.pd4ml.PD4ML;
//
//import java.util.concurrent.CompletableFuture;
//
//
//public class test {
//    protected int topValue = 10;
//    protected int leftValue = 20;
//    protected int rightValue = 10;
//    int bottomValue = 10;
//    protected int userSpaceWidth = 1300;
//
//    @Test
//    public void Object() throws IOException {
//        String endpoint = "oss-cn-hangzhou.aliyuncs.com";
//        String accessKeyId = "LTAI5tJoCbDpuFjjZn54VE8U";
//        String accessKeySecret = "w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF";
//        String bucketName = "testcjg";
//        String path = "bb54139a.png";
//        File file =new File("C:\\opt\\metersphere\\data\\image\\markdown\\bb54139a.png");
//        FileSystemResource fileResource = new FileSystemResource(file);
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//        PutObjectResult putObjectResult = ossClient.putObject(bucketName, path, fileResource.getInputStream());
//        putObjectResult.getETag();
//    }
//
////    public static void main(String[] args) throws IOException {
//////        try {
//////            test jt = new test();
//////
//////            jt.doConversion("file:///C:/Users/111/Desktop/test.html", "C:\\Users\\111\\Desktop\\test.rtf");
//////        } catch (Exception e) {
//////            e.printStackTrace();
//////        }
////        String html = "<article class=\\\"4ever-article\\\"><p style=\\\"text-align:left;text-indent:0;margin-left:0;margin-top:0;margin-bottom:0\\\"><span data-type=\\\"text\\\">测试desc</span></p><p style=\\\"text-align:left;text-indent:0;margin-left:0;margin-top:0;margin-bottom:0\\\"><span data-type=\\\"text\\\"></span><img src=\\\"https://devops.aliyun.com/projex/api/workitem/file/url?fileIdentifier=8c79ff7329141b8fd4f508cd29\\\" style=\\\"width:528px;height:83.656050955414px\\\"/><span data-type=\\\"text\\\"></span></p></article>";
////        ByteArrayInputStream bais =
////                new ByteArrayInputStream(html.getBytes());
////        PD4ML pd4ml = new PD4ML();
////// read and parse HTML
////
////        pd4ml.readHTML(bais);
////        File rtf = File.createTempFile("result", ".rtf");
////        FileOutputStream fos = new FileOutputStream(rtf);
////// render and write the result as PDF
////        pd4ml.writeRTF(fos, true);
//
////        pd4ml.setHtmlWidth(userSpaceWidth); // set frame width of "virtual web browser"
////
////        // A4格式
////        pd4ml.setPageSize(pd4ml.changePageOrientation(PD4Constants.A4));
////
////        // 定义边距
////        pd4ml.setPageInsetsMM(new Insets(topValue, leftValue, bottomValue, rightValue));
////
////
////        //字体添加
////        String classPath = Test.class.getResource("/")+"fonts";
////        pd4ml.useTTF(classPath, true);
////        //默认字体
////        //  pd4ml.setDefaultTTFs("Kingsoft", "", "");
////
////        // 强制生成RTF
////        pd4ml.outputFormat(PD4Constants.RTF_WMF);
//
////    }
//}
//
////    public void doConversion( String url, String outputPath )
////            throws InvalidParameterException, MalformedURLException, IOException {
////        File output = new File(outputPath);
////        //写入文件
////        java.io.FileOutputStream fos = new java.io.FileOutputStream(output);
////
////        PD4ML pd4ml = new PD4ML();
////
////        pd4ml.setHtmlWidth(userSpaceWidth); // set frame width of "virtual web browser"
////
////        // A4格式
////        pd4ml.setPageSize(pd4ml.changePageOrientation(PD4Constants.A4));
////
////        // 定义边距
////        pd4ml.setPageInsetsMM(new Insets(topValue, leftValue, bottomValue, rightValue));
////
////
////        //字体添加
////        String classPath = Test.class.getResource("/")+"fonts";
////        pd4ml.useTTF(classPath, true);
////        //默认字体
////        //  pd4ml.setDefaultTTFs("Kingsoft", "", "");
////
////        // 强制生成RTF
////        pd4ml.outputFormat(PD4Constants.RTF_WMF);
////
////        //  从URL到RTF文件的实际文档转换
////        pd4ml.render(new URL(url), fos);
////        fos.close();
////
////        System.out.println( outputPath + "\ndone." );
////    }
////
////}
////    public static void main(String[] args) throws Exception {
////
////        // HttpClient Configuration
////        /*HttpClient httpClient = new ApacheAsyncHttpClientBuilder()
////                .connectionTimeout(Duration.ofSeconds(10)) // Set the connection timeout time, the default is 10 seconds
////                .responseTimeout(Duration.ofSeconds(10)) // Set the response timeout time, the default is 20 seconds
////                .maxConnections(128) // Set the connection pool size
////                .maxIdleTimeOut(Duration.ofSeconds(50)) // Set the connection pool timeout, the default is 30 seconds
////                // Configure the proxy
////                .proxy(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("<your-proxy-hostname>", 9001))
////                        .setCredentials("<your-proxy-username>", "<your-proxy-password>"))
////                // If it is an https connection, you need to configure the certificate, or ignore the certificate(.ignoreSSL(true))
////                .x509TrustManagers(new X509TrustManager[]{})
////                .keyManagers(new KeyManager[]{})
////                .ignoreSSL(false)
////                .build();*/
////
////        // Configure Credentials authentication information, including ak, secret, token
////        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
////                // Please ensure that the environment variables ALIBABA_CLOUD_ACCESS_KEY_ID and ALIBABA_CLOUD_ACCESS_KEY_SECRET are set.
////                .accessKeyId(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"))
////                .accessKeySecret(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"))
////                //.securityToken(System.getenv("ALIBABA_CLOUD_SECURITY_TOKEN")) // use STS token
////                .build());
////
////        // Configure the Client
////        AsyncClient client = AsyncClient.builder()
////                .region("cn-hangzhou") // Region ID
////                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
////                .credentialsProvider(provider)
////                //.serviceConfiguration(Configuration.create()) // Service-level configuration
////                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
////                .overrideConfiguration(
////                        ClientOverrideConfiguration.create()
////                                // Endpoint 请参考 https://api.aliyun.com/product/devops
////                                .setEndpointOverride("devops.cn-hangzhou.aliyuncs.com")
////                        //.setConnectTimeout(Duration.ofSeconds(30))
////                )
////                .build();
////
////        // Parameter settings for API request
////        CreateWorkitemV2Request createWorkitemV2Request = CreateWorkitemV2Request.builder()
////                // Request-level configuration rewrite, can set Http request parameters, etc.
////                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
////                .build();
////
////        // Asynchronously get the return value of the API request
////        CompletableFuture<CreateWorkitemV2Response> response = client.createWorkitemV2(createWorkitemV2Request);
////        // Synchronously get the return value of the API request
////        CreateWorkitemV2Response resp = response.get();
//////        System.out.println(new Gson().toJson(resp));
////        // Asynchronous processing of return values
////        /*response.thenAccept(resp -> {
////            System.out.println(new Gson().toJson(resp));
////        }).exceptionally(throwable -> { // Handling exceptions
////            System.out.println(throwable.getMessage());
////            return null;
////        });*/
////
////        // Finally, close the client
////        client.close();
////    }
//
//
//
////    public static com.aliyun.devops20210625.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
////        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
////                // 必填，您的 AccessKey ID
////                .setAccessKeyId(accessKeyId)
////                // 必填，您的 AccessKey Secret
////                .setAccessKeySecret(accessKeySecret);
////        // Endpoint 请参考 https://api.aliyun.com/product/devops
////        config.endpoint = "devops.cn-hangzhou.aliyuncs.com";
////        return new com.aliyun.devops20210625.Client(config);
////    }
////
////
////    public static void main(String[] args_) throws Exception {
////        java.util.List<String> args = java.util.Arrays.asList(args_);
////        // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
////        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
////        com.aliyun.devops20210625.Client client = createClient("LTAI5tJoCbDpuFjjZn54VE8U", "w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF");
////        java.util.Map<String, String> headers = new java.util.HashMap<>();
////        try {
////            // 复制代码运行请自行打印 API 的返回值
////            ListProjectsRequest listProjectsRequest = new ListProjectsRequest();
////            listProjectsRequest.setCategory("Project");
////            ListProjectsResponse listProjectsResponse = client.listProjects("64ad2260702c0cacad9a1f2a",listProjectsRequest);
////            System.out.println(JSON.toJSONString(listProjectsResponse.getBody()));
//////            client.skipPipelineJobRunWithOptions("64ad2260702c0cacad9a1f2a", "1", "", "", headers, new com.aliyun.teautil.models.RuntimeOptions());
////        } catch (TeaException error) {
////            // 如有需要，请打印 error
////            com.aliyun.teautil.Common.assertAsString(error.message);
////        } catch (Exception _error) {
////            TeaException error = new TeaException(_error.getMessage(), _error);
////            // 如有需要，请打印 error
////            com.aliyun.teautil.Common.assertAsString(error.message);
////        }
////    }
//
////        public static void main(String[] args) throws Exception {
//
//            // HttpClient Configuratipublic static com.aliyun.devops20210625.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
//            //        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
//            //                // 必填，您的 AccessKey ID
//            //                .setAccessKeyId(accessKeyId)
//            //                // 必填，您的 AccessKey Secret
//            //                .setAccessKeySecret(accessKeySecret);
//            //        // Endpoint 请参考 https://api.aliyun.com/product/devops
//            //        config.endpoint = "devops.cn-hangzhou.aliyuncs.com";
//            //        return new com.aliyun.devops20210625.Client(config);
//            //    }
//            //
//            //    public static void main(String[] args_) throws Exception {
//            //        java.util.List<String> args = java.util.Arrays.asList(args_);
//            //        // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
//            //        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
//            //        com.aliyun.devops20210625.Client client = Sample.createClient(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"), System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"));
//            //        java.util.Map<String, String> headers = new java.util.HashMap<>();
//            //        try {
//            //            // 复制代码运行请自行打印 API 的返回值
//            //            client.skipPipelineJobRunWithOptions("your_value", "1", "", "", headers, new com.aliyun.teautil.models.RuntimeOptions());
//            //        } catch (TeaException error) {
//            //            // 如有需要，请打印 error
//            //            com.aliyun.teautil.Common.assertAsString(error.message);
//            //        } catch (Exception _error) {
//            //            TeaException error = new TeaException(_error.getMessage(), _error);
//            //            // 如有需要，请打印 error
//            //            com.aliyun.teautil.Common.assertAsString(error.message);
//            //        }
////                }
//
////    }
//
