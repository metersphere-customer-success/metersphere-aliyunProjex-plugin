//package io.metersphere;
//
//import com.aliyun.auth.credentials.Credential;
//import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
//import com.aliyun.core.http.HttpClient;
//import com.aliyun.core.http.HttpMethod;
//import com.aliyun.core.http.ProxyOptions;
//import com.aliyun.httpcomponent.httpclient.ApacheAsyncHttpClientBuilder;
//import com.aliyun.sdk.service.oss20190517.models.*;
//import com.aliyun.sdk.service.oss20190517.*;
//import com.google.gson.Gson;
//import darabonba.core.RequestConfiguration;
//import darabonba.core.client.ClientOverrideConfiguration;
//import darabonba.core.utils.CommonUtil;
//import darabonba.core.TeaPair;
//import org.springframework.core.io.FileSystemResource;
//
////import javax.net.ssl.KeyManager;
////import javax.net.ssl.X509TrustManager;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.util.concurrent.CompletableFuture;
//
//public class PutObject {
//    public static void main(String[] args) throws Exception {
//
//        // HttpClient Configuration
//        /*HttpClient httpClient = new ApacheAsyncHttpClientBuilder()
//                .connectionTimeout(Duration.ofSeconds(10)) // Set the connection timeout time, the default is 10 seconds
//                .responseTimeout(Duration.ofSeconds(10)) // Set the response timeout time, the default is 20 seconds
//                .maxConnections(128) // Set the connection pool size
//                .maxIdleTimeOut(Duration.ofSeconds(50)) // Set the connection pool timeout, the default is 30 seconds
//                // Configure the proxy
//                .proxy(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("<your-proxy-hostname>", 9001))
//                        .setCredentials("<your-proxy-username>", "<your-proxy-password>"))
//                // If it is an https connection, you need to configure the certificate, or ignore the certificate(.ignoreSSL(true))
//                .x509TrustManagers(new X509TrustManager[]{})
//                .keyManagers(new KeyManager[]{})
//                .ignoreSSL(false)
//                .build();*/
//
//        // Configure Credentials authentication information, including ak, secret, token
//        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
//                // Please ensure that the environment variables ALIBABA_CLOUD_ACCESS_KEY_ID and ALIBABA_CLOUD_ACCESS_KEY_SECRET are set.
//                .accessKeyId("LTAI5tJoCbDpuFjjZn54VE8U")
//                .accessKeySecret("w7gvqxNvoH5iCFU1bKzo6uhg1rhbiF")
//                //.securityToken(System.getenv("ALIBABA_CLOUD_SECURITY_TOKEN")) // use STS token
//                .build());
//
//        // Configure the Client
//        AsyncClient client = AsyncClient.builder()
//                .region("cn-hangzhou") // Region ID
//                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
//                .credentialsProvider(provider)
//                //.serviceConfiguration(Configuration.create()) // Service-level configuration
//                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
//                .overrideConfiguration(
//                        ClientOverrideConfiguration.create()
//                                // Endpoint 请参考 https://oss-cn-hangzhou.aliyuncs.com
//                                .setEndpointOverride("oss-cn-hangzhou.aliyuncs.com")
//                        //.setConnectTimeout(Duration.ofSeconds(30))
//                )
//                .build();
//        File file = new File("C:\\opt\\metersphere\\data\\image\\markdown\\bb54139a.png");
//        FileSystemResource fileResource = new FileSystemResource(file);
//        ByteArrayInputStream a = new ByteArrayInputStream(fileResource.getInputStream().readAllBytes());
//        // Parameter settings for API request
//        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                .bucket("testcjg")
//                .key("bb54139a.png")
//                .body(a)
//                // Request-level configuration rewrite, can set Http request parameters, etc.
//                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
//                .build();
//
//        // Asynchronously get the return value of the API request
//        CompletableFuture<PutObjectResponse> response = client.putObject(putObjectRequest);
//        // Synchronously get the return value of the API request
//        PutObjectResponse resp = response.get();
//        System.out.println(new Gson().toJson(resp));
//        // Asynchronous processing of return values
//        /*response.thenAccept(resp -> {
//            System.out.println(new Gson().toJson(resp));
//        }).exceptionally(throwable -> { // Handling exceptions
//            System.out.println(throwable.getMessage());
//            return null;
//        });*/
//
//        // Finally, close the client
//        client.close();
//    }
//
//}